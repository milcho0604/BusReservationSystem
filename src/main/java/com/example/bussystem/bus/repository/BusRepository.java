package com.example.bussystem.bus.repository;

import com.example.bussystem.bus.domain.Bus;
import com.example.bussystem.category.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRepository extends JpaRepository<Bus, Long> {
    Page<Bus> findAll(Pageable pageable);

}
