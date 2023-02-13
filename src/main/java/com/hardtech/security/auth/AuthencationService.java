package com.hardtech.security.auth;

import com.hardtech.security.config.JwtService;
import com.hardtech.security.user.Role;
import com.hardtech.security.user.User;
import com.hardtech.security.user.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthencationService {

    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final JwtService jwtService;
    final AuthenticationManager authenticationManager;

    private AuthenticationResponse generateToken(User user) {
        var token = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(token).build();
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
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
            return generateToken(user);
        } else {
            throw new UserAlraydyExistException(String.format("User %s alraydy exists !", request.getEmail()));
        }


    }

    public AuthenticationResponse authenticate(AuthencationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),
                request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        return generateToken(user);
    }
}
