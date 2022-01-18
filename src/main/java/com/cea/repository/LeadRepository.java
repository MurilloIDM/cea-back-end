package com.cea.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cea.models.Lead;

public interface LeadRepository extends JpaRepository<Lead, UUID> {
	
	@Query
	Lead findByEmail(String email);
	@Query
	Lead findByPhone(String phone);
}
