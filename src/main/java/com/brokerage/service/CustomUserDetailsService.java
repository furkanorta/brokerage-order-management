package com.brokerage.service;

import com.brokerage.entity.Customer;
import com.brokerage.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername (username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        return new User(customer.getUsername(), customer.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
    
    public UserDetails loadUserByCustomerId (String customerId) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByCustomerId (customerId)
            .orElseThrow(() -> new UsernameNotFoundException("Customer not found: " + customerId));
        
        return new User(customer.getUsername(), customer.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

}
