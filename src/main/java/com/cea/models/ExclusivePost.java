package com.cea.models;

import com.cea.enums.TypeExclusivePost;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_exclusive_post")
public class ExclusivePost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;
    @Enumerated(EnumType.STRING)
    private TypeExclusivePost type;
    private String title;
    private String description;
    private boolean status;
    private boolean filed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    @OneToMany(mappedBy = "exclusivePost")
    private List<Media> media;
    @OneToMany(mappedBy = "exclusivePost")
    private List<PollTopics> pollTopics;

}
