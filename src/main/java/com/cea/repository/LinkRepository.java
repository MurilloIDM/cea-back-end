package com.cea.repository;

import com.cea.models.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LinkRepository extends JpaRepository<Link, UUID> {

    List<Link> findByExclusivePost_Id(UUID exclusivePostId);

}
