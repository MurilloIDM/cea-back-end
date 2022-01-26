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
	private String imageUrl;
	private Boolean status;
	private String user;

	public FreePost toEntity() {

		FreePost freePost = new FreePost();

		Date date = new Date();

		freePost.setTitle(title);
		freePost.setDescription(description);
		freePost.setImageUrl(imageUrl);
		freePost.setStatus(status);
		freePost.setCreatedAt(date);
		freePost.setUpdatedAt(date);
		freePost.setCreatedBy(user);
		freePost.setUpdatedBy(user);

		return freePost;

	}

}
