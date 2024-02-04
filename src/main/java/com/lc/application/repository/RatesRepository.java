package com.lc.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lc.application.model.Rates;

public interface RatesRepository extends JpaRepository<Rates, Long> {
}