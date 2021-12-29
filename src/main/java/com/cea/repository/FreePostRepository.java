package com.cea.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cea.models.FreePost;

public interface FreePostRepository extends JpaRepository<FreePost, UUID> {

	Page<FreePost> findByTitleContaining(String title, Pageable pageRequest);

}
