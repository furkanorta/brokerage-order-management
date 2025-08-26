package com.brokerage.service;

import com.brokerage.dto.CreateOrderRequest;
import com.brokerage.entity.Asset;
import com.brokerage.entity.Order;
import com.brokerage.entity.OrderSide;
import com.brokerage.entity.OrderStatus;
import com.brokerage.repository.AssetRepository;
import com.brokerage.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private OrderService orderService;

    private CreateOrderRequest alimEmri;
    private CreateOrderRequest satimEmri;
    private Asset paraVarlik;
    private Asset hisseVarlik;

    @BeforeEach
    void setUp () {
        alimEmri = new CreateOrderRequest();
        alimEmri.setCustomerId("CUST001");
        alimEmri.setAsset("AAPL");
        alimEmri.setSide(OrderSide.BUY);
        alimEmri.setSize(new BigDecimal("10"));
        alimEmri.setPrice(new BigDecimal("150.00"));

        satimEmri = new CreateOrderRequest();
        satimEmri.setCustomerId("CUST001");
        satimEmri.setAsset("AAPL");
        satimEmri.setSide(OrderSide.SELL);
        satimEmri.setSize(new BigDecimal("5"));
        satimEmri.setPrice(new BigDecimal("160.00"));

        paraVarlik = new Asset("CUST001", "TRY", new BigDecimal("10000.00"), new BigDecimal("10000.00"));
        hisseVarlik = new Asset("CUST001", "AAPL", new BigDecimal("100.00"), new BigDecimal("100.00"));
    }

    @Test
    void alimEmriOlustur_Basarili () {
        when(assetRepository.findByCustomerIdAndAssetName("CUST001", "TRY"))
            .thenReturn(Optional.of(paraVarlik));
        when(orderRepository.save(any(Order.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(assetRepository.save(any(Asset.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Order sonuc = orderService.createOrder (alimEmri);

        assertNotNull(sonuc);
        assertEquals(OrderStatus.PENDING, sonuc.getStatus());
        assertEquals(OrderSide.BUY, sonuc.getOrderSide());
        assertEquals("AAPL", sonuc.getAssetName());
        assertEquals(new BigDecimal("10"), sonuc.getSize());
        assertEquals(new BigDecimal("150.00"), sonuc.getPrice());

        verify(assetRepository, times(1)).save(any(Asset.class));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void alimEmri_YetersizBakiye_HataVerir () {
        paraVarlik.setUsableSize(new BigDecimal("1000.00"));
        when(assetRepository.findByCustomerIdAndAssetName("CUST001", "TRY"))
            .thenReturn(Optional.of(paraVarlik));

        RuntimeException hata = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder (alimEmri);
        });

        assertTrue(hata.getMessage().contains("Yetersiz TRY bakiyesi"));
    }

    @Test
    void satimEmriOlustur_Basarili () {
        when(assetRepository.findByCustomerIdAndAssetName("CUST001", "AAPL"))
            .thenReturn(Optional.of(hisseVarlik));
        when(orderRepository.save(any(Order.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(assetRepository.save(any(Asset.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Order sonuc = orderService.createOrder (satimEmri);
        assertNotNull(sonuc);
        assertEquals(OrderStatus.PENDING, sonuc.getStatus());
        assertEquals(OrderSide.SELL, sonuc.getOrderSide());
        assertEquals("AAPL", sonuc.getAssetName());
        assertEquals(new BigDecimal("5"), sonuc.getSize());
        assertEquals(new BigDecimal("160.00"), sonuc.getPrice());

        verify(assetRepository, times(1)).save(any(Asset.class));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void satimEmri_YetersizVarlik_HataVerir () {
        hisseVarlik.setUsableSize(new BigDecimal("2.00"));
        when(assetRepository.findByCustomerIdAndAssetName("CUST001", "AAPL"))
            .thenReturn(Optional.of(hisseVarlik));

        RuntimeException hata = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder (satimEmri);
        });

        assertTrue(hata.getMessage().contains("Yetersiz AAPL bakiyesi"));
    }
}
