package com.lc.application.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lc.application.model.Rates;

public interface RatesRepository extends JpaRepository<Rates, Long> {

	Page<Rates> findAll(Pageable pageable);

	Page<Rates> findByNameContainingIgnoreCase(String name, Pageable pageable);
}