package com.lc.application.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lc.application.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	Page<User> findByEmailContainingIgnoreCase(String email, Pageable pageable);
}