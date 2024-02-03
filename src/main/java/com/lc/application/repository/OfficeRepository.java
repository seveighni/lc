package com.lc.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lc.application.model.Office;

public interface OfficeRepository extends JpaRepository<Office, Long> {
   
}