package com.example.test.repository;

import com.example.test.model.OrderHistory;
import com.example.test.model.ShoppingCart;
import com.example.test.model.ShoppingCartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Integer> {
}
