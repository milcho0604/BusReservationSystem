package com.example.bussystem.category.repository;

import com.example.bussystem.bus.domain.Bus;
import com.example.bussystem.category.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Bus> {
    Page<Category> findAll(Pageable pageable);
    Optional<Category> findByCategoryName(String categoryName);

}
