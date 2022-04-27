package com.cea.repository;

import com.cea.models.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MediaRepository extends JpaRepository<Media, UUID> {

    List<Media> findByExclusivePost_Id(UUID exclusivePostId);

}
