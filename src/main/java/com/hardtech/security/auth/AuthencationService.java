package com.hardtech.security.auth;

import com.hardtech.security.auth.exceptions.UserNotFound;
import com.hardtech.security.auth.requests.*;
import com.hardtech.security.config.JwtService;
import com.hardtech.security.user.Role;
import com.hardtech.security.user.User;
import com.hardtech.security.user.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthencationService extends CurrentUser {

    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final JwtService jwtService;
    final AuthenticationManager authenticationManager;

    private AuthenticationResponse generateToken(User user) {
        return jwtService.generateTokens(user);
    }

    public AuthenticationResponse register(RegisterRequest request) {
        //test si n'existe pas encore
        var alraydy = userRepository.findByEmail(request.getEmail());
        if (alraydy.isEmpty()) {
            var user = User.builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .roles(List.of(Role.USER, Role.ADMIN))
                    .build();
            userRepository.save(user);
            return generateToken(user);
        } else {
            //throw new UserAlraydyExistException(String.format("%s alraydy used !", request.getEmail()));
            return null;
        }
    }

    public AuthenticationResponse authenticate(AuthencationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFound("User not found"));
        return generateToken(user);
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
