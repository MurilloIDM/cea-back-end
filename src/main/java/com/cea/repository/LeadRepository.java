package com.cea.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cea.models.Lead;

public interface LeadRepository extends JpaRepository<Lead, UUID> {}
