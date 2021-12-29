package com.cea.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cea.dto.FreePostDTO;
import com.cea.models.FreePost;
import com.cea.repository.FreePostRepository;

@Service
public class FreePostService {

	@Autowired
	FreePostRepository freePostRepository;

	/* * * * *
	 * INSERT
	 * * * * */
	public FreePost insert(FreePostDTO freePostDTO) {
		FreePost freePost = freePostDTO.toEntity();

		Date date = new Date();
		freePost.setCreatedAt(date);

		return freePostRepository.save(freePost);
	}

	/* * * * *
	 * UPDATE
	 * * * * */
	public FreePost update(UUID id, FreePostDTO freePostDTO) {
		FreePost existingFreePost = freePostRepository.findById(id).get();

		updateData(existingFreePost, freePostDTO);

		return freePostRepository.save(existingFreePost);
	}

	/* * * * *
	 * FINDALL
	 * * * * */
	public List<FreePost> findAll() {
		return freePostRepository.findAll();
	}

	/* * * * *
	 * FINDONE
	 * * * * */
	public FreePost findOne(UUID id) {
		return freePostRepository.findById(id).get();
	}

	/* * * * *
	 * DELETE
	 * * * * */
	public void delete(UUID id) {
		freePostRepository.deleteById(id);
	}

	/******************************************************************************/
	private void updateData(FreePost existingFreePost, FreePostDTO freePostDTO) {
		FreePost freePost = freePostDTO.toEntity();

		Date date = new Date();

		existingFreePost.setTitle(freePost.getTitle());
		existingFreePost.setDescription(freePost.getDescription());
		existingFreePost.setStatus(freePost.getStatus());
		existingFreePost.setUpdatedAt(date);
		existingFreePost.setUpdatedBy(freePost.getUpdatedBy());
	}

}
