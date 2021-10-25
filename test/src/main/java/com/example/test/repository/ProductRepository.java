package com.example.test.repository;

import com.example.test.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
    //Product findById(String productID);

    //@Query("select p from Product p where p.name like ?1")
    Iterable<Product> findProductsByNameContaining(String name);
    Iterable<Product> findByPriceBetween(Float start, Float end);
    Page<Product> findAllByVisibility(Pageable page,Integer visibility);
    Page<Product> findAllByIsDeleted(Pageable page, Integer isDeleted);
    Page<Product> findByBrand(Pageable page, String brand);
    Page<Product> findByPriceIsGreaterThan(Pageable page, Float minPrice);
    Page<Product> findByPriceIsLessThan(Pageable page, Float maxPrice);
    Page<Product> findByPriceBetween(Pageable page, Float minPrice, Float maxPrice);
    Page<Product> findByBrandAndPriceIsGreaterThan(Pageable page, String brand, Float minPrice);
    Page<Product> findByBrandAndPriceIsLessThan(Pageable page, String brand, Float maxPrice);
    Page<Product> findByBrandAndPriceBetween(Pageable page, String brand, Float minPrice, Float maxPrice);
}
