package com.hardtech.security.auth.controllers;

import com.hardtech.security.auth.requests.*;
import com.hardtech.security.auth.services.AuthencationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthencationService authencationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        Boolean response = authencationService.register(request);
        if (Objects.isNull(response))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Email alraydy used");
        else
            return ResponseEntity.ok().body(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthencationRequest request) {
        AuthenticationResponse response = authencationService.authenticate(request);
        if (Objects.isNull(response))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Bad email or password");
        else if (response.getAccess_token().equals(response.getRefresh_token()))
            return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body("Please, confirm your email");
        else {
            return ResponseEntity.ok().body(response);
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordRequest passwordRequest) {
        UserResponse response = authencationService.changePassword(passwordRequest);
        if (Objects.nonNull(response)) {
            return ResponseEntity.ok().body(response);
        }
        return ResponseEntity.badRequest().body("Password don't match");
    }

    @GetMapping
    public UserResponse profile() {
        return authencationService.profile();
    }

    @GetMapping("/verify/{token}")
    public boolean verifyEmail(@PathVariable String token) {
        authencationService.verifyAccount(token);
        return true;
    }

}
