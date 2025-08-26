package com.brokerage.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "customer_id", nullable = false)
    private String customerId;
    
    @Column(name = "asset_name", nullable = false)
    private String assetName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "order_side", nullable = false)
    private OrderSide orderSide;
    
    @Column(name = "size", nullable = false, precision = 19, scale = 4)
    private BigDecimal size;
    
    @Column(name = "price", nullable = false, precision = 19, scale = 4)
    private BigDecimal price;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;
    
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;
    
    public Order() {
        this.createDate = LocalDateTime.now();
    }
    
    public Order (String customerId, String assetName, OrderSide orderSide, 
                BigDecimal size, BigDecimal price) {
        this();
        this.customerId = customerId;
        this.assetName = assetName;
        this.orderSide = orderSide;
        this.size = size;
        this.price = price;
        this.status = OrderStatus.PENDING;
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
    
    public OrderSide getOrderSide() {
        return orderSide;
    }
    
    public void setOrderSide (OrderSide orderSide) {
        this.orderSide = orderSide;
    }
    
    public BigDecimal getSize() {
        return size;
    }
    
    public void setSize (BigDecimal size) {
        this.size = size;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice (BigDecimal price) {
        this.price = price;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus (OrderStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreateDate() {
        return createDate;
    }
    
    public void setCreateDate (LocalDateTime createDate) {
        this.createDate = createDate;
    }

}
