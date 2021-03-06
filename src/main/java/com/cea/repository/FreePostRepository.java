package com.cea.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cea.models.FreePost;
import org.springframework.stereotype.Repository;

@Repository
public interface FreePostRepository extends JpaRepository<FreePost, UUID> {

	Page<FreePost> findByIgnoreCaseTitleContaining(String title, Pageable pageRequest);
	List<FreePost> findByStatusTrue();
	Page<FreePost> findByIgnoreCaseTitleContainingAndStatusIs(String title, Boolean status, Pageable pageRequest);
	Page<FreePost> findByStatusIs(Boolean status, Pageable pageRequest);

}
