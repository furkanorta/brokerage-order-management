package com.brokerage.service;

import com.brokerage.dto.CreateOrderRequest;
import com.brokerage.entity.Asset;
import com.brokerage.entity.Order;
import com.brokerage.entity.OrderSide;
import com.brokerage.entity.OrderStatus;
import com.brokerage.repository.AssetRepository;
import com.brokerage.repository.OrderRepository;
//import java.util.ArrayList;
//import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private AssetRepository assetRepository;
    
    public Order createOrder (CreateOrderRequest request) {
        validateOrder(request);
        
        Order order = new Order();
        order.setCustomerId (request.getCustomerId());
        order.setAssetName (request.getAsset());
        order.setOrderSide (request.getSide());
        order.setSize (request.getSize());
        order.setPrice (request.getPrice());
        order.setStatus (OrderStatus.PENDING);
        order.setCreateDate (LocalDateTime.now());
        
        varlikBakiyeleriniGuncelle (request);
        
        return orderRepository.save(order);
    }
    
    private void validateOrder (CreateOrderRequest request) {
        if (request.getSide() == OrderSide.BUY) {
            Asset musteriPara = varlikGetirVeyaOlustur(request.getCustomerId(), "TRY");
            BigDecimal toplamMaliyet = request.getSize().multiply(request.getPrice());
            
            if (musteriPara.getUsableSize().compareTo(toplamMaliyet) < 0) {
                throw new RuntimeException("Yetersiz TRY bakiyesi. Gerekli: " + toplamMaliyet + ", Mevcut: " + musteriPara.getUsableSize());
            }
        } else {
            Asset musteriVarlik = varlikGetirVeyaOlustur(request.getCustomerId(), request.getAsset());
            
            if (musteriVarlik.getUsableSize().compareTo(request.getSize()) < 0) {
                throw new RuntimeException("Yetersiz " + request.getAsset() + " bakiyesi. Gerekli: " + request.getSize() + ", Mevcut: " + musteriVarlik.getUsableSize());
            }
        }
    }
    
    private void varlikBakiyeleriniGuncelle (CreateOrderRequest request) {
        if (request.getSide() == OrderSide.BUY) {
            Asset musteriPara = varlikGetirVeyaOlustur(request.getCustomerId(), "TRY");
            BigDecimal toplamMaliyet = request.getSize().multiply(request.getPrice());
            musteriPara.setUsableSize (musteriPara.getUsableSize().subtract(toplamMaliyet));
            assetRepository.save(musteriPara);
        } else {
            Asset musteriVarlik = varlikGetirVeyaOlustur(request.getCustomerId(), request.getAsset());
            musteriVarlik.setUsableSize (musteriVarlik.getUsableSize().subtract(request.getSize()));
            assetRepository.save(musteriVarlik);
        }
    }
    
    private Asset varlikGetirVeyaOlustur (String customerId, String assetName) {
        return assetRepository.findByCustomerIdAndAssetName (customerId, assetName)
            .orElseGet(() -> {
                Asset yeniVarlik = new Asset(customerId, assetName, BigDecimal.ZERO, BigDecimal.ZERO);
                return assetRepository.save(yeniVarlik);
            });
    }
    
    public List<Order> getOrders (String customerId, LocalDateTime startDate, LocalDateTime endDate, OrderStatus status) {
        return orderRepository.findOrdersWithFilters (customerId, startDate, endDate, status);
    }
    
    public List<Order> getOrders (String customerId) {
        return orderRepository.findByCustomerId (customerId);
    }
    
    public void cancelOrder (Long orderId, String customerId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        
        if (orderOpt.isEmpty()) {
            throw new RuntimeException("Order not found");
        }
        
        Order order = orderOpt.get();
        
        if (!order.getCustomerId().equals(customerId)) {
            throw new RuntimeException("Order does not belong to customer");
        }
        
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only pending orders can be canceled");
        }
        
        order.setStatus (OrderStatus.CANCELED);
        orderRepository.save(order);
        
        restoreAssetBalances (order);
    }
    
    private void restoreAssetBalances (Order order) {
        if (order.getOrderSide() == OrderSide.BUY) {
            Asset musteriPara = varlikGetirVeyaOlustur(order.getCustomerId(), "TRY");
            BigDecimal geriYuklenenMiktar = order.getSize().multiply(order.getPrice());
            musteriPara.setUsableSize (musteriPara.getUsableSize().add(geriYuklenenMiktar));
            assetRepository.save(musteriPara);
        } else {
            Asset musteriVarlik = varlikGetirVeyaOlustur(order.getCustomerId(), order.getAssetName());
            musteriVarlik.setUsableSize (musteriVarlik.getUsableSize().add(order.getSize()));
            assetRepository.save(musteriVarlik);
        }
    }
    
    public List<Order> getPendingOrders() {
        return orderRepository.findByStatus (OrderStatus.PENDING);
    }
    
    public void matchOrder (Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        
        if (orderOpt.isEmpty()) {
            throw new RuntimeException("Order not found");
        }
        
        Order order = orderOpt.get();
        
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only pending orders can be matched");
        }
        
        order.setStatus (OrderStatus.MATCHED);
        orderRepository.save(order);
        
        updateAssetBalancesForMatchedOrder (order);
    }
    
    private void updateAssetBalancesForMatchedOrder (Order order) {
        if (order.getOrderSide() == OrderSide.BUY) {
            Asset musteriVarlik = varlikGetirVeyaOlustur(order.getCustomerId(), order.getAssetName());
            musteriVarlik.setSize (musteriVarlik.getSize().add(order.getSize()));
            musteriVarlik.setUsableSize (musteriVarlik.getUsableSize().add(order.getSize()));
            assetRepository.save(musteriVarlik);
        } else {
            Asset musteriPara = varlikGetirVeyaOlustur(order.getCustomerId(), "TRY");
            BigDecimal kazanilanMiktar = order.getSize().multiply(order.getPrice());
            musteriPara.setSize (musteriPara.getSize().add(kazanilanMiktar));
            musteriPara.setUsableSize (musteriPara.getUsableSize().add(kazanilanMiktar));
            assetRepository.save(musteriPara);
        }
    }

}
