package com.cea.repository;

import com.cea.models.Administrator;
import com.cea.models.CommentReply;
import com.cea.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

@Repository
public interface CommentReplyRepository extends JpaRepository<CommentReply, UUID> {

    Optional<CommentReply> findByIdAndStudent(UUID id, Student student);
    List<CommentReply> findAllByComment_IdAndStatusTrue(UUID commentId);
    Page<CommentReply> findAllByCommentIdAndStatusTrue(UUID commentId, Pageable pageRequest);

}
