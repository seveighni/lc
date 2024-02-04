package com.lc.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lc.application.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}