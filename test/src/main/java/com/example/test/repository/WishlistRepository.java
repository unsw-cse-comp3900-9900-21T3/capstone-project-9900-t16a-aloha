package com.example.test.repository;

import com.example.test.model.Wishlist;
import com.example.test.model.WishlistId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, WishlistId> {

}
