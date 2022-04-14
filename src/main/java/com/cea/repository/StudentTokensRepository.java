package com.cea.repository;

import com.cea.models.StudentTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentTokensRepository extends JpaRepository<StudentTokens, UUID> {}
