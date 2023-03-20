package com.hardtech.security.auth.services;

import com.hardtech.mails.MailService;
import com.hardtech.mails.NotificationEmail;
import com.hardtech.security.auth.config.JwtService;
import com.hardtech.security.auth.exceptions.UserNotFound;
import com.hardtech.security.auth.requests.*;
import com.hardtech.security.entities.tokens.VerificationToken;
import com.hardtech.security.entities.user.Role;
import com.hardtech.security.entities.user.User;
import com.hardtech.security.repositories.UserRepository;
import com.hardtech.security.repositories.VerificationTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthencationService extends CurrentUser {

    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final JwtService jwtService;
    final AuthenticationManager authenticationManager;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

    private AuthenticationResponse generateToken(User user) {
        return jwtService.generateTokens(user);
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found with " +
                "name - " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new RuntimeException("Invalid Token")));
    }

    public Boolean register(RegisterRequest request) {
        //test si n'existe pas encore
        var alraydy = userRepository.findByEmail(request.getEmail());
        if (alraydy.isEmpty()) {
            var user = User.builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .roles(List.of(Role.USER))
                    .isEnabled(false)
                    .build();
            userRepository.save(user);


            String token = generateVerificationToken(user);

            String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/auth")
                    .path("/verify/")
                    .path(token)
                    .toUriString();

            mailService.sendMail(NotificationEmail.builder()
                    .subject("Please Activate your Account")
                    .recipient(user.getEmail())
                    .body("Thank you for signing up to my application, " +
                            "please click on the below url to activate your account : " + url)
                    .build());
            return true;
        } else {
            //throw new UserAlraydyExistException(String.format("%s alraydy used !", request.getEmail()));
            return false;
        }
    }

    public AuthenticationResponse authenticate(AuthencationRequest request) {

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFound("User not found"));

        if (Objects.nonNull(user)) {
            if (user.isEnabled()) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
                return generateToken(user);
            } else {
                log.warn("Please, confirm you email");
                return AuthenticationResponse.builder().access_token("null").refresh_token("null").build();
            }
        }
        return null;
    }

    public UserResponse changePassword(PasswordRequest passwordRequest) {
        if (passwordEncoder.matches(passwordRequest.getOldPassword(), currentUser().getPassword())) {
            currentUser().setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
            return userToUserResponse(userRepository.save(currentUser()));
        }
        return null;
    }

    public UserResponse profile() {
        return userToUserResponse(currentUser());
    }

    UserResponse userToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .build();
    }
}
