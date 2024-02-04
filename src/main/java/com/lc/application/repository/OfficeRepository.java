package com.lc.application.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lc.application.model.Office;

public interface OfficeRepository extends JpaRepository<Office, Long> {
    Page<Office> findByAddressContainingIgnoreCase(String keyword, Pageable pageable);
}