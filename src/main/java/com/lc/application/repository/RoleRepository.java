package com.lc.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lc.application.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}