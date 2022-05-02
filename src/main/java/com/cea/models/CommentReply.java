package com.cea.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_comment_reply")
public class CommentReply implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;
    private String text;
    private boolean status;
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = true)
    private Student student;
    @ManyToOne
    @JoinColumn(name = "administrator_id", nullable = true)
    private Administrator administrator;

}
