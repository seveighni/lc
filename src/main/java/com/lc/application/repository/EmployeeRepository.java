package com.lc.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lc.application.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	List<Employee> findAll();

	Page<Employee> findByUserEmailContainingIgnoreCase(String keyword, Pageable pageable);

	Optional<Employee> getEmployeeIdByUserEmail(String employeeEmail);
}
