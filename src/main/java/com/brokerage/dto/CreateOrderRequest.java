package com.brokerage.dto;

import com.brokerage.entity.OrderSide;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CreateOrderRequest {
    
    @NotBlank(message = "Customer ID is required")
    private String customerId;
    
    @NotBlank(message = "Asset name is required")
    private String asset;
    
    @NotNull(message = "Order side is required")
    private OrderSide side;
    
    @NotNull(message = "Size is required")
    @DecimalMin(value = "0.0001", message = "Size must be greater than 0")
    private BigDecimal size;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId (String customerId) {
        this.customerId = customerId;
    }
    
    public String getAsset() {
        return asset;
    }
    
    public void setAsset (String asset) {
        this.asset = asset;
    }
    
    public OrderSide getSide() {
        return side;
    }
    
    public void setSide (OrderSide side) {
        this.side = side;
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

}
