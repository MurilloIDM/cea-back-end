package com.cea.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cea.models.Administrator;

public interface AdministratorRepository extends JpaRepository<Administrator, UUID> {
	
	Administrator findByUsername(String username);
	Page<Administrator> findByNameContaining(String name, Pageable pageRequest);
	
}
