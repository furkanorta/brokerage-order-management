package com.brokerage.config;

import com.brokerage.entity.Asset;
import com.brokerage.entity.Customer;
import com.brokerage.repository.AssetRepository;
import com.brokerage.repository.CustomerRepository;
//import org.springframework.boot.ApplicationArguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private AssetRepository assetRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run (String... args) throws Exception {
        if (customerRepository.count() == 0) {
            Customer customer1 = new Customer("CUST001", "customer1", 
                passwordEncoder.encode("password123"), "John Doe", "john@example.com");
            customerRepository.save(customer1);
            
            Customer customer2 = new Customer("CUST002", "customer2", 
                passwordEncoder.encode("password123"), "Jane Smith", "jane@example.com");
            customerRepository.save(customer2);
            

            Asset tryAsset1 = new Asset("CUST001", "TRY", new BigDecimal("10000.00"), new BigDecimal("10000.00"));
            assetRepository.save(tryAsset1);
            
            Asset aaplAsset1 = new Asset("CUST001", "AAPL", new BigDecimal("100.00"), new BigDecimal("100.00"));
            assetRepository.save(aaplAsset1);
            
            Asset tryAsset2 = new Asset("CUST002", "TRY", new BigDecimal("5000.00"), new BigDecimal("5000.00"));
            assetRepository.save(tryAsset2);
            
            Asset googlAsset2 = new Asset("CUST002", "GOOGL", new BigDecimal("50.00"), new BigDecimal("50.00"));
            assetRepository.save(googlAsset2);
        }
    }

}
