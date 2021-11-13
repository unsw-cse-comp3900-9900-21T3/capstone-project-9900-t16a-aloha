package com.example.test.repository;

import com.example.test.model.Storge;
import com.example.test.model.StorgeId;
import org.springframework.data.repository.CrudRepository;

public interface StorgeRepository extends CrudRepository<Storge, StorgeId> {
}
