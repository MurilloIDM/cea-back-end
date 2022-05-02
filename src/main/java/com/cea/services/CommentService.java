package com.cea.services;

import com.cea.dto.comment.CommentDTO;
import com.cea.models.Comment;
import com.cea.models.CommentReply;
import com.cea.models.ExclusivePost;
import com.cea.models.Student;
import com.cea.repository.CommentRepository;
import com.cea.repository.ExclusivePostRepository;
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
    private final ExclusivePostRepository exclusivePostRepository;
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

}
