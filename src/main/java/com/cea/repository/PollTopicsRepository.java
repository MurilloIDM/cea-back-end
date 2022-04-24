package com.cea.repository;

import com.cea.models.PollTopics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PollTopicsRepository extends JpaRepository<PollTopics, UUID> {

    List<PollTopics> findByExclusivePost_Id(UUID exclusivePostId);

}
