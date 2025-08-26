package com.brokerage.controller;

import com.brokerage.dto.LoginRequest;
import com.brokerage.entity.Customer;
import com.brokerage.repository.CustomerRepository;
import com.brokerage.security.JwtTokenProvider;
//import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    public ResponseEntity<?> login (@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken (authentication);
        
        Map<String, String> response = new HashMap<>();
        response.put("token", jwt);
        response.put("type", "Bearer");
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Register a new customer")
    public ResponseEntity<?> register (@RequestBody Customer customer) {
        if (customerRepository.findByUsername (customer.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        
        if (customerRepository.findByCustomerId (customer.getCustomerId()).isPresent()) {
            return ResponseEntity.badRequest().body("Customer ID already exists");
        }
        
        customer.setPassword (passwordEncoder.encode(customer.getPassword()));
        Customer savedCustomer = customerRepository.save(customer);
        
        return ResponseEntity.ok("Customer registered successfully: " + savedCustomer.getCustomerId());
    }

}
