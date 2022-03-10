package com.cea.models;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_administrator")
public class Administrator implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private UUID id;
	
	private String name;
	
	private String username;
	
	@JsonIgnore
	private String password;
	
	private Boolean roles;
	
	private Boolean isPrimaryAccess;
	
	private Date createdAt;
	
	private Date updatedAt;
	
	private String createdBy;
	
	private String updatedBy;

}
