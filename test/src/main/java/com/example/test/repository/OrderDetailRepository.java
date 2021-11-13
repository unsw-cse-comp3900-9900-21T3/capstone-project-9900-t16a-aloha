package com.example.test.repository;

import com.example.test.model.OrderDetail;
import com.example.test.model.OrderId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderId> {
    Iterable<OrderDetail> findByOrderId_OrderHistory_Id(Integer id);
}
