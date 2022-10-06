package com.cea.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_link")
public class Link implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;
    private String label;
    private String url;
    @ManyToOne
    @JoinColumn(name = "exclusive_post_id", nullable = false)
    @JsonIgnore
    private ExclusivePost exclusivePost;

}
