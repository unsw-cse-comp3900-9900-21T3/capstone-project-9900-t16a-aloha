package com.example.test.repository;

import com.example.test.model.Product;
import com.example.test.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    Product findById(String productID);

    //@Query("select p from Product p where p.name like ?1")
    Iterable<Product> findProductsByNameContaining(String name);
}
