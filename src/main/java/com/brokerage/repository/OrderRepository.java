package com.brokerage.repository;

import com.brokerage.entity.Order;
import com.brokerage.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByCustomerId (String customerId);
    
    List<Order> findByCustomerIdAndCreateDateBetween (String customerId, LocalDateTime startDate, LocalDateTime endDate);
    
    List<Order> findByStatus (OrderStatus status);
    
    List<Order> findByCustomerIdAndStatus (String customerId, OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.customerId = :customerId " +
           "AND (:startDate IS NULL OR o.createDate >= :startDate) " +
           "AND (:endDate IS NULL OR o.createDate <= :endDate) " +
           "AND (:status IS NULL OR o.status = :status)")
    List<Order> findOrdersWithFilters (@Param("customerId") String customerId,
                                     @Param("startDate") LocalDateTime startDate,
                                     @Param("endDate") LocalDateTime endDate,
                                     @Param("status") OrderStatus status);

}
