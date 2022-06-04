package com.cea.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cea.models.Administrator;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, UUID> {
	
	Administrator findByUsername(String username);
	Page<Administrator> findByIgnoreCaseNameContaining(String name, Pageable pageRequest);
	
}
