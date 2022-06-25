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
@Table(name = "tb_poll_topics")
public class PollTopics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;
    private String description;
    @Column(nullable = true)
    private int totalVotes;
    @ManyToOne()
    @JoinColumn(name = "exclusive_post_id", nullable = false)
    @JsonIgnore
    private ExclusivePost exclusivePost;
    private LocalDateTime updatedAt;

}
