package com.cea.repository;

import com.cea.models.Comment;
import com.cea.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    Optional<Comment> findByIdAndStudent(UUID id, Student student);
    List<Comment> findAllByExclusivePost_IdAndStatusTrue(UUID exclusivePostId);

}
