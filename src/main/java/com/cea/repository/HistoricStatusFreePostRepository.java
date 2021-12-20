package com.cea.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cea.models.HistoricStatusFreePost;

public interface HistoricStatusFreePostRepository extends JpaRepository<HistoricStatusFreePost, UUID> {}