package com.cea.models;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Administrator implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "BINARY(16)")
	private UUID id;
	
	private String name;
	
	private String username;
	
	@JsonIgnore
	private String password;
	
	private Boolean roles;
	
	private Date createdAt;
	
	private Date updatedAt;
	
	private String createdBy;
	
	private String updatedBy;

}
