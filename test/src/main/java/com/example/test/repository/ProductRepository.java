package com.example.test.repository;

import com.example.test.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.criteria.CriteriaBuilder;

public interface ProductRepository extends JpaRepository<Product, String> {
    //Product findById(String productID);
    Page<Product> findByIdAndIsDeleted(Pageable page, String id, Integer isDeleted);
    //@Query("select p from Product p where p.name like ?1")
    Iterable<Product> findProductsByNameContaining(String name);
    Iterable<Product> findByPriceBetween(Float start, Float end);

    // user query: consider visibility and isDeleted
    Page<Product> findAllByVisibility(Pageable page,Integer visibility);
    Page<Product> findAllByIsDeleted(Pageable page, Integer isDeleted);
    Page<Product> findByBrandAndVisibilityAndIsDeleted(Pageable page, String brand, Integer visibility, Integer isDeleted);
    Page<Product> findByPriceIsGreaterThanAndVisibilityAndIsDeleted(Pageable page, Float minPrice, Integer visibility, Integer isDeleted);
    Page<Product> findByPriceIsLessThanAndVisibilityAndIsDeleted(Pageable page, Float maxPrice, Integer visibility, Integer isDeleted);
    Page<Product> findByPriceBetweenAndVisibilityAndIsDeleted(Pageable page, Float minPrice, Float maxPrice, Integer visibility, Integer isDeleted);
    Page<Product> findByBrandAndPriceIsGreaterThanAndVisibilityAndIsDeleted(Pageable page, String brand, Float minPrice, Integer visibility, Integer isDeleted);
    Page<Product> findByBrandAndPriceIsLessThanAndVisibilityAndIsDeleted(Pageable page, String brand, Float maxPrice, Integer visibility, Integer isDeleted);
    Page<Product> findByBrandAndPriceBetweenAndVisibilityAndIsDeleted(Pageable page, String brand, Float minPrice, Float maxPrice, Integer visibility, Integer isDeleted);

    // admin query: consider isDeleted
    Page<Product> findByPriceIsLessThanAndIsDeleted(Pageable page, Float maxPrice, Integer isDeleted);
    Page<Product> findByPriceIsGreaterThanAndIsDeleted(Pageable page, Float minPrice, Integer isDeleted);
    Page<Product> findByPriceBetweenAndIsDeleted(Pageable page, Float minPrice, Float maxPrice, Integer isDeleted);
    Page<Product> findByNameContainsAndPriceIsGreaterThanAndIsDeleted(Pageable page, String name, Float minPrice, Integer isDeleted);
    Page<Product> findByNameContainsAndPriceIsLessThanAndIsDeleted(Pageable page, String name, Float maxPrice, Integer isDeleted);
    Page<Product> findByNameContainsAndPriceIsBetweenAndIsDeleted(Pageable page, String name, Float minPrice, Float maxPrice, Integer isDeleted);
    Page<Product> findByNameContainsAndIsDeleted(Pageable page, String name, Integer isDeleted);
    // admin sort
    Page<Product> findByPriceIsLessThanAndIsDeletedOrderByNameDesc(Pageable page, Float maxPrice, Integer isDeleted);
}
