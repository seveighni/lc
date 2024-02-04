package com.lc.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lc.application.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
	List<Employee> findAll();
}
