package com.digiron.hype.v1.controller;

import com.digiron.hype.data.model.User;
import com.digiron.hype.data.repository.RoleRepository;
import com.digiron.hype.data.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
@Slf4j
public class UserController {
    private final UserRepository userRepository;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/user")
    public User getUser() {
        return userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public @NotNull List<User> getUsers() {
        return userRepository.findAll();
    }
}
