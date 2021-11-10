package com.example.test.repository;

import com.example.test.model.OrderHistory;
import com.example.test.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Review findByOrderHistory(OrderHistory orderHistory);
}
