package com.example.test.repository;

import com.example.test.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * TODO: change the product Id to string and change the method inside the adminController
 */

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findById(String productID);

    //@Query("select p from Product p where p.name like ?1")
    Iterable<Product> findProductsByNameContaining(String name);
    Iterable<Product> findByPriceBetween(Float start, Float end);
}
