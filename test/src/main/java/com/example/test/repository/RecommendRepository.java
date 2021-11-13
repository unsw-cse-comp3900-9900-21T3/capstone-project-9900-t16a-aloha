package com.example.test.repository;

import com.example.test.model.Product;
import com.example.test.model.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecommendRepository extends JpaRepository<Recommend, String> {
    @Query(nativeQuery=true, value="SELECT *  FROM recommend ORDER BY RAND() LIMIT 1")
    Recommend findRandom();
}
