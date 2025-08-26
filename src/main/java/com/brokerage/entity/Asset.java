package com.brokerage.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "assets")
public class Asset {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "customer_id", nullable = false)
    private String customerId;
    
    @Column(name = "asset_name", nullable = false)
    private String assetName;
    
    @Column(name = "size", nullable = false, precision = 19, scale = 4)
    private BigDecimal size;
    
    @Column(name = "usable_size", nullable = false, precision = 19, scale = 4)
    private BigDecimal usableSize;
    
    public Asset() {
    }
    
    public Asset (String customerId, String assetName, BigDecimal size, BigDecimal usableSize) {
        this.customerId = customerId;
        this.assetName = assetName;
        this.size = size;
        this.usableSize = usableSize;
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
    
    public String getAssetName() {
        return assetName;
    }
    
    public void setAssetName (String assetName) {
        this.assetName = assetName;
    }
    
    public BigDecimal getSize() {
        return size;
    }
    
    public void setSize (BigDecimal size) {
        this.size = size;
    }
    
    public BigDecimal getUsableSize() {
        return usableSize;
    }
    
    public void setUsableSize (BigDecimal usableSize) {
        this.usableSize = usableSize;
    }

}
