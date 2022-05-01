package com.cea.repository;

import com.cea.models.PollTopics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PollTopicsRepository extends JpaRepository<PollTopics, UUID> {

    List<PollTopics> findByExclusivePost_Id(UUID exclusivePostId);

}
