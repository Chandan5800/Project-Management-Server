package com.PMS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PMS.config.JwtProvider;
import com.PMS.model.User;
import com.PMS.repository.UserRepository;
import com.PMS.request.LoginRequest;
import com.PMS.response.AuthResponse;
import com.PMS.service.CustomUserDetailsImpl;
import com.PMS.service.SubscriptionService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsImpl customUserDetailsImpl;

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
        // Check if the email already exists in the database
        User isUserExist = userRepository.findByEmail(user.getEmail());
        if (isUserExist != null) {
            throw new Exception("Email already exist with another account"); 
        }
        User createdUser = new User();
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
        createdUser.setEmail(user.getEmail());
        createdUser.setFullName(user.getFullName());

        User savedUser = userRepository.save(createdUser);
        subscriptionService.createSubscription(savedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse();
        res.setMessage("signup successfully");
        res.setJwt(jwt);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) {

        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = JwtProvider.generateToken(authentication);
        AuthResponse res = new AuthResponse();
        res.setMessage("signin successfully");
        res.setJwt(jwt);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserDetailsImpl.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}

