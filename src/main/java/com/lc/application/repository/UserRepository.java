package com.lc.application.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lc.application.model.Role;
import com.lc.application.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	Page<User> findByRoles(Role roles, Pageable pageable);

	Page<User> findByEmailContainingIgnoreCase(String email, Pageable pageable);

	Page<User> findByRolesAndEmailContainingIgnoreCase(Role roles, String email, Pageable pageable);
}