package com.cea.services;

import com.cea.dto.comment.CommentDTO;
import com.cea.dto.comment.CommentReplyDTO;
import com.cea.models.*;
import com.cea.repository.*;
import com.cea.utils.LocalDateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentReplyRepository commentReplyRepository;
    private final ExclusivePostRepository exclusivePostRepository;
    private final StudentRepository studentRepository;
    private final AdministratorRepository administratorRepository;
    private final StudentService studentService;
    private final LocalDateTimeUtils localDateTimeUtils;


    public void addComment(CommentDTO payload) {
        UUID studentId = payload.getStudentId();
        UUID exclusivePostId = payload.getExclusivePostId();

        Student student = this.studentService.findById(studentId);
        Optional<ExclusivePost> exclusivePost = this.exclusivePostRepository.findById(exclusivePostId);

        if (exclusivePost.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Conteúdo não encontrado!");
        }

        if (!exclusivePost.get().isStatus() || exclusivePost.get().isFiled()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Conteúdo inválido para inserir comentário!");
        }

        if (!exclusivePost.get().getType().name().equalsIgnoreCase("TEXT")) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST, "O tipo da publicação não pode receber comentário!");
        }

        String text = payload.getText();
        LocalDateTime dateNow = this.localDateTimeUtils.dateNow();

        Comment comment = new Comment();
        comment.setText(text);
        comment.setStatus(true);
        comment.setStudent(student);
        comment.setCreatedAt(dateNow);
        comment.setExclusivePost(exclusivePost.get());

        this.commentRepository.save(comment);
    }

    public void addCommentReply(CommentReplyDTO payload) {
        boolean isAdmin = false;
        boolean isStudent = true;

        Optional<Student> student = null;
        Optional<Administrator> administrator = null;

        UUID userId = payload.getUserId();
        UUID commentId = payload.getCommentId();

        student = this.studentRepository.findById(userId);

        if (student.isEmpty()) {
            administrator = this.administratorRepository.findById(userId);

            if (administrator.isEmpty()) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Estudante/Administrator não encontrado!");
            }

            isAdmin = true;
            isStudent = false;
        }

        Optional<Comment> comment = this.commentRepository.findById(commentId);

        if (comment.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Comentário não encontrado!");
        }

        ExclusivePost exclusivePost = comment.get().getExclusivePost();

        if (!exclusivePost.isStatus() || exclusivePost.isFiled()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Conteúdo inválido para inserir comentário!");
        }

        String text = payload.getText();
        LocalDateTime dateNow = this.localDateTimeUtils.dateNow();

        CommentReply commentReply = new CommentReply();
        commentReply.setText(text);
        commentReply.setStatus(true);
        commentReply.setCreatedAt(dateNow);
        commentReply.setComment(comment.get());

        if (isAdmin) {
            commentReply.setAdministrator(administrator.get());
        } else {
            commentReply.setStudent(student.get());
        }

        this.commentReplyRepository.save(commentReply);
    }

}
