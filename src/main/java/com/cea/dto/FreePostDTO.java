package com.cea.dto;

import java.io.Serializable;
import java.util.Date;

import com.cea.models.FreePost;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FreePostDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String title;
	private String description;
	private Boolean status;
	private Date createdAt;
	private Date updatedAt;
	private String createdBy;
	private String updatedBy;

	public FreePost toEntity() {

		FreePost freePost = new FreePost();

		freePost.setTitle(title);
		freePost.setDescription(description);
		freePost.setStatus(status);
		freePost.setCreatedAt(createdAt);
		freePost.setUpdatedAt(updatedAt);
		freePost.setCreatedBy(createdBy);
		freePost.setUpdatedBy(updatedBy);

		return freePost;

	}

}
