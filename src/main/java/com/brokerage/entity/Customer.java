package com.brokerage.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "customer_id", nullable = false, unique = true)
    private String customerId;
    
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "email")
    private String email;
    
    public Customer() {
    }
    
    public Customer (String customerId, String username, String password, String name, String email) {
        this.customerId = customerId;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId (Long id) {
        this.id = id;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId (String customerId) {
        this.customerId = customerId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername (String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword (String password) {
        this.password = password;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName (String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail (String email) {
        this.email = email;
    }

}
