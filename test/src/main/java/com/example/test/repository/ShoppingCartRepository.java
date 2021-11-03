package com.example.test.repository;

import com.example.test.model.ShoppingCart;
import com.example.test.model.ShoppingCartId;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, ShoppingCartId> {
    Iterable<ShoppingCart> findByShoppingCartId_User_Id(Integer id);

}
