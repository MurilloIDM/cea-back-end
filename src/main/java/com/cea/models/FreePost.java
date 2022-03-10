package com.cea.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_freePost")
public class FreePost implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "id")
	private UUID id;
	private String title;
	@Column(columnDefinition = "TEXT")
	private String description;
	private String imageUrl;
	private Boolean status;
	private Date createdAt;
	private Date updatedAt;
	private String createdBy;
	private String updatedBy;

	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "freePost")
	private List<HistoricStatusFreePost> historicStatusFreePost = new ArrayList<>();

}
