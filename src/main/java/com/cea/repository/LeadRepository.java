package com.cea.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cea.models.Lead;

public interface LeadRepository extends JpaRepository<Lead, UUID> {
	
	Lead findByEmail(String email);
	Lead findByPhone(String phone);
	Lead findByDeviceId (UUID deviceId);
}
