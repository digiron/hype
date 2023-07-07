package com.digiron.hype.v1.controller;

import com.digiron.hype.data.model.User;
import com.digiron.hype.data.repository.RoleRepository;
import com.digiron.hype.data.repository.UserRepository;
import com.digiron.hype.security.UserDetailsImpl;
import com.digiron.hype.security.jwt.JwtUtils;
import com.digiron.hype.v1.requests.AdminSignupRequest;
import com.digiron.hype.v1.requests.LoginRequest;
import com.digiron.hype.v1.requests.SignupRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthorizationController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    @Value("${applicationAdminPassword}")
    private String applicationAdminPassword;

    @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.info("Signin result: {}", authentication.isAuthenticated());
        return ResponseEntity.ok().header(SET_COOKIE, jwtUtils.generateJwtCookie(userDetails).toString())
                .body(userDetails);
    }

    @PostMapping(value="/signout")
    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.ok().header(SET_COOKIE, jwtUtils.getCleanJwtCookie().toString()).build();
    }

    //todo: I am not sure that this belongs here. I think we forward/call crud endpoint for user entities, then login
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return createUserWithRole(signUpRequest, "ROLE_USER");
    }

    @PostMapping(value = "/adminsignup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminSignupRequest signUpRequest) {
        log.info("comparing {} and {}", applicationAdminPassword, signUpRequest.getApplicationAdminPassword());
        if (!signUpRequest.getApplicationAdminPassword().equals(applicationAdminPassword)) {
            return ResponseEntity.badRequest().body("incorrect admin password");
        }
        return createUserWithRole(signUpRequest, "ROLE_ADMIN");
    }

    private ResponseEntity<?> createUserWithRole(SignupRequest signUpRequest, String roleUser) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }
        userRepository.save(User.from(signUpRequest, Set.of(roleRepository.findByName(roleUser)), encoder));
        return authenticateUser(signUpRequest.toLoginRequest());
    }
}