package com.cea.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cea.models.FreePost;
import com.cea.repository.FreePostRepository;

@Service
public class FreePostService {

	@Autowired
	FreePostRepository freePostRepository;

	
	/* * * * *
	 * INSERT
	 * * * * */
	public FreePost insert(FreePost freePost) {
		Date date = new Date();
		freePost.setCreatedAt(date);
		// freepost.setUpdatedAt(date);
		return freePostRepository.save(freePost);
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
	 * UPDATE
	 * * * * */
	public FreePost update(UUID id, FreePost freePost) {
		Date date = new Date();
		freePost.setId(id);
		freePost.setUpdatedAt(date);
		return freePostRepository.save(freePost);
	}
	
	/* * * * *
	 * UPDATE
	 * * * * */
	public void delete(UUID id) {
		freePostRepository.deleteById(id);
	}
	

}
