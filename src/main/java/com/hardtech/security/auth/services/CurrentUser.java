package com.hardtech.security.auth.services;

import com.hardtech.security.entities.user.User;
import com.hardtech.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUser {

    @Autowired
    private UserRepository userRepository;

    public User currentUser() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(currentUsername).get();
    }

}