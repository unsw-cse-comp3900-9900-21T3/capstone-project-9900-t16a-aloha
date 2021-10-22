package com.example.test.repository;

import com.example.test.model.ShoppingCart;
import com.example.test.model.ShoppingCartId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, ShoppingCartId> {
    Iterable<ShoppingCart> findByShoppingCartId_User_Id(Integer id);
}
