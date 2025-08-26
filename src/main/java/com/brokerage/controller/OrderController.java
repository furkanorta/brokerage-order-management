package com.brokerage.controller;

import com.brokerage.dto.CreateOrderRequest;
import com.brokerage.entity.Order;
import com.brokerage.entity.OrderStatus;
import com.brokerage.service.OrderService;
//import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Orders", description = "Order management endpoints")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @PostMapping
    @Operation(summary = "Create order", description = "Create a new order for a customer")
    public ResponseEntity<Order> createOrder (@Valid @RequestBody CreateOrderRequest request) {
        Order order = orderService.createOrder (request);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping
    @Operation(summary = "List orders", description = "Get orders with optional filters")
    public ResponseEntity<List<Order>> getOrders (
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) OrderStatus status) {
        
        if (customerId == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            customerId = auth.getName();
        }
        
        List<Order> orders = orderService.getOrders (customerId, startDate, endDate, status);
        return ResponseEntity.ok(orders);
    }
    
    @DeleteMapping("/{orderId}")
    @Operation(summary = "Cancel order", description = "Cancel a pending order")
    public ResponseEntity<?> cancelOrder (@PathVariable Long orderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String customerId = auth.getName();
        
        orderService.cancelOrder (orderId, customerId);
        return ResponseEntity.ok("Cancel ok");
    }
    
    @GetMapping("/pending")
    @Operation(summary = "Get pending orders", description = "Get all pending orders (Admin only)")
    public ResponseEntity<List<Order>> getPendingOrders() {
        List<Order> pendingOrders = orderService.getPendingOrders();
        return ResponseEntity.ok(pendingOrders);
    }
    
    @PostMapping("/{orderId}/match")
    @Operation(summary = "Match order", description = "Match a pending order (Admin only)")
    public ResponseEntity<?> matchOrder (@PathVariable Long orderId) {
        orderService.matchOrder (orderId);
        return ResponseEntity.ok("Order matched");
    }

}
