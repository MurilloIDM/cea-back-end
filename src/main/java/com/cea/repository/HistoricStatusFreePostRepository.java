package com.cea.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cea.models.HistoricStatusFreePost;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricStatusFreePostRepository extends JpaRepository<HistoricStatusFreePost, UUID> {}