package com.cea.repository;

import com.cea.models.ExclusivePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExclusivePostRepository extends JpaRepository<ExclusivePost, UUID> {}
