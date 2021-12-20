package com.cea.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cea.models.UserLead;

public interface LeadRepository extends JpaRepository<UserLead, UUID> {}
