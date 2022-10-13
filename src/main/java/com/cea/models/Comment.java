package com.cea.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "tb_comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;
    private String text;
    private boolean status;
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @JsonIgnore
    private Student student;
    @ManyToOne
    @JoinColumn(name = "administrator_id", nullable = true)
    @JsonIgnore
    private Administrator administrator;
    @ManyToOne
    @JoinColumn(name = "exclusive_post_id", nullable = false)
    @JsonIgnore
    private ExclusivePost exclusivePost;
}
