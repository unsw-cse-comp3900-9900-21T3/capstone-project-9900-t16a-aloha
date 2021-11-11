package com.example.test.repository;

import com.example.test.model.Product;
import com.example.test.model.Storge;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.criteria.CriteriaBuilder;

public interface ProductRepository extends JpaRepository<Product, String> {

    @Query(nativeQuery=true, value="SELECT *  FROM product ORDER BY RAND() LIMIT 1")
    Product findRandom();
    //Product findById(String productID);
    Page<Product> findByIdAndIsDeleted(Pageable page, String id, Integer isDeleted);
    Page<Product> findByIdAndVisibilityAndIsDeleted(Pageable page, String id, Integer visibility, Integer isDeleted);
    //@Query("select p from Product p where p.name like ?1")
    Iterable<Product> findProductsByNameContaining(String name);
    Iterable<Product> findByPriceBetween(Float start, Float end);

    // user query: consider visibility and isDeleted
    Page<Product> findAllByVisibility(Pageable page,Integer visibility);
    Page<Product> findAllByVisibilityAndIsDeleted(Pageable page, Integer visibility, Integer isDeleted);
    Page<Product> findAllByIsDeleted(Pageable page, Integer isDeleted);
    Page<Product> findByBrandAndVisibilityAndIsDeleted(Pageable page, String brand, Integer visibility, Integer isDeleted);
    Page<Product> findByPriceIsGreaterThanAndVisibilityAndIsDeleted(Pageable page, Float minPrice, Integer visibility, Integer isDeleted);
    Page<Product> findByPriceIsLessThanAndVisibilityAndIsDeleted(Pageable page, Float maxPrice, Integer visibility, Integer isDeleted);
    Page<Product> findByPriceBetweenAndVisibilityAndIsDeleted(Pageable page, Float minPrice, Float maxPrice, Integer visibility, Integer isDeleted);
    Page<Product> findByBrandAndPriceIsGreaterThanAndVisibilityAndIsDeleted(Pageable page, String brand, Float minPrice, Integer visibility, Integer isDeleted);
    Page<Product> findByBrandAndPriceIsLessThanAndVisibilityAndIsDeleted(Pageable page, String brand, Float maxPrice, Integer visibility, Integer isDeleted);
    Page<Product> findByBrandAndPriceBetweenAndVisibilityAndIsDeleted(Pageable page, String brand, Float minPrice, Float maxPrice, Integer visibility, Integer isDeleted);
    Page<Product> findByNameContainsAndVisibilityAndIsDeleted(Pageable page, String name, Integer visibility, Integer isDeleted);
    Page<Product> findByNameContainsAndBrandAndVisibilityAndIsDeleted(Pageable page, String name, String brand, Integer visibility, Integer isDeleted);
    Page<Product> findByNameContainsAndPriceIsLessThanAndVisibilityAndIsDeleted(Pageable page, String name, Float maxPrice, Integer visibility, Integer isDeleted);
    Page<Product> findByNameContainsAndPriceIsGreaterThanAndVisibilityAndIsDeleted(Pageable page, String name, Float maxPrice, Integer visibility, Integer isDeleted);
    Page<Product> findByNameContainsAndPriceIsBetweenAndVisibilityAndIsDeleted(Pageable page, String name, Float minPrice,Float maxPrice, Integer visibility, Integer isDeleted);
    Page<Product> findByBrandAndPriceIsBetweenAndVisibilityAndIsDeleted(Pageable page, String brand, Float minPrice, Float maxPrice, Integer visibility, Integer isDeleted);
    Page<Product> findByNameContainsAndBrandAndPriceIsLessThanAndVisibilityAndIsDeleted(Pageable page, String name, String brand, Float maxPrice, Integer visibility, Integer isDeleted);
    Page<Product> findByNameContainsAndBrandAndPriceIsGreaterThanAndVisibilityAndIsDeleted(Pageable page, String name, String brand, Float minPrice, Integer visibility, Integer isDeleted);
    Page<Product> findByNameContainsAndBrandAndPriceIsBetweenAndVisibilityAndIsDeleted(Pageable page, String name, String brand, Float minPrice, Float maxPrice, Integer visibility, Integer isDeleted);
    // user query: instock filter
    Page<Product> findDistinctAllByVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(Pageable page,Integer visibility, Integer isDeleted,Integer stock);
    Page<Product> findByIdAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(Pageable page, String id, Integer visibility, Integer isDeleted, Integer stock);
    Page<Product> findDistinctByBrandAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(Pageable page, String brand, Integer visibility, Integer isDeleted, Integer stock);
    Page<Product> findDistinctByPriceIsLessThanAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(Pageable page, Float maxPrice, Integer visibility, Integer isDeleted, Integer stock);
    Page<Product> findDistinctByPriceIsGreaterThanAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(Pageable page, Float minPrice, Integer visibility, Integer isDeleted, Integer stock);
    Page<Product> findDistinctByPriceBetweenAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(Pageable page, Float minPrice, Float maxPrice, Integer visibility, Integer isDeleted, Integer stock);
    Page<Product> findDistinctByBrandAndPriceIsGreaterThanAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(Pageable page, String brand, Float minPrice, Integer visibility, Integer isDeleted, Integer stock);
    Page<Product> findDistinctByBrandAndPriceIsLessThanAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(Pageable page, String brand, Float maxPrice, Integer visibility, Integer isDeleted, Integer stock);
    Page<Product> findDistinctByBrandAndPriceBetweenAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(Pageable page, String brand, Float minPrice, Float maxPrice, Integer visibility, Integer isDeleted, Integer stock);
    Page<Product> findDistinctByNameContainsAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(Pageable page, String name, Integer visibility, Integer isDeleted, Integer stock);
    Page<Product> findDistinctByNameContainsAndBrandAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(Pageable page, String name, String brand, Integer visibility, Integer isDeleted, Integer stock);
    Page<Product> findDistinctByNameContainsAndPriceIsLessThanAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(Pageable page, String name, Float maxPrice, Integer visibility, Integer isDeleted, Integer stock);
    Page<Product> findDistinctByNameContainsAndPriceIsGreaterThanAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(Pageable page, String name, Float maxPrice, Integer visibility, Integer isDeleted, Integer stock);
    Page<Product> findDistinctByNameContainsAndPriceIsBetweenAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(Pageable page, String name, Float minPrice,Float maxPrice, Integer visibility, Integer isDeleted, Integer stock);
    Page<Product> findDistinctByBrandAndPriceIsBetweenAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(Pageable page, String brand, Float minPrice, Float maxPrice, Integer visibility, Integer isDeleted, Integer stock);
    Page<Product> findDistinctByNameContainsAndBrandAndPriceIsLessThanAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(Pageable page, String name, String brand, Float maxPrice, Integer visibility, Integer isDeleted, Integer stock);
    Page<Product> findDistinctByNameContainsAndBrandAndPriceIsGreaterThanAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(Pageable page, String name, String brand, Float minPrice, Integer visibility, Integer isDeleted, Integer stock);
    Page<Product> findDistinctByNameContainsAndBrandAndPriceIsBetweenAndVisibilityAndIsDeletedAndStorge_stockIsGreaterThan(Pageable page, String name, String brand, Float minPrice, Float maxPrice, Integer visibility, Integer isDeleted, Integer stock);
    // admin query: consider isDeleted
    Page<Product> findByNameContainsAndBrandAndPriceIsBetweenAndIsDeleted(Pageable page, String name, String brand, Float minPrice, Float maxPrice, Integer isDeleted);
    Page<Product> findByNameContainsAndBrandAndPriceIsLessThanAndIsDeleted(Pageable page, String name, String brand, Float maxPrice, Integer isDeleted);
    Page<Product> findByBrandAndIsDeleted(Pageable page, String brand, Integer isDeleted);
    Page<Product> findByNameContainsAndBrandAndPriceIsGreaterThanAndIsDeleted(Pageable page, String name, String brand, Float minPrice, Integer isDeleted);
    Page<Product> findByBrandAndPriceIsGreaterThanAndIsDeleted(Pageable page, String brand, Float minPrice, Integer isDeleted);
    Page<Product> findByBrandAndPriceIsLessThanAndIsDeleted(Pageable page, String brand, Float maxPrice, Integer isDeleted);
    Page<Product> findByBrandAndPriceBetweenAndIsDeleted(Pageable page, String brand, Float minPrice, Float maxPrice, Integer isDeleted);
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
