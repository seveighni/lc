package com.lc.application.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lc.application.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Page<Customer> findByUserEmailContainingIgnoreCase(String email, PageRequest paging);

	Optional<Customer> getCustomerIdByUserEmail(String customerEmail);

}