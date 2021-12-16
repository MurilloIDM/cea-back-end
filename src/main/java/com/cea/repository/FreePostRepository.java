package com.cea.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cea.models.FreePost;

public interface FreePostRepository extends JpaRepository<FreePost, UUID> {

}
