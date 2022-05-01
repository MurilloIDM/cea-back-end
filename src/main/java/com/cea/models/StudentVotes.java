package com.cea.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_student_votes")
public class StudentVotes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    private Student student;
    @ManyToOne
    private ExclusivePost exclusivePost;
    @ManyToOne
    private PollTopics pollTopics;
}
