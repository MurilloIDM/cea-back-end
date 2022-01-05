package com.cea.services;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.cea.dto.FreePostDTO;
import com.cea.models.FreePost;
import com.cea.models.HistoricStatusFreePost;
import com.cea.repository.FreePostRepository;

@Service
public class FreePostService {

	@Autowired
	FreePostRepository freePostRepository;

	@Autowired
	HistoricStatusFreePostService historicStatusFreePostService;

	/* * * * *
	 * INSERT
	 * * * * */
	public FreePost insert(FreePostDTO freePostDTO) {

		FreePost freePost = freePostDTO.toEntity();
		HistoricStatusFreePost historicStatusFreePost = new HistoricStatusFreePost();

		updateHistoric(historicStatusFreePost, freePostDTO);

		freePost.getHistoricStatusFreePost().add(historicStatusFreePost);

		historicStatusFreePostService.insert(historicStatusFreePost);

		return freePostRepository.save(freePost);
	}

	/* * * * * 
	 * UPDATE
	 * * * * */
	public FreePost update(UUID id, FreePostDTO freePostDTO) {
		FreePost existingFreePost = findById(id);
		

		if (existingFreePost.getStatus() != freePostDTO.getStatus()) {
			HistoricStatusFreePost historicStatusFreePost = new HistoricStatusFreePost();
			updateHistoric(historicStatusFreePost, freePostDTO);
			historicStatusFreePostService.insert(historicStatusFreePost);
			existingFreePost.getHistoricStatusFreePost().add(historicStatusFreePost);
		}

		updateData(existingFreePost, freePostDTO);

		return freePostRepository.save(existingFreePost);
	}

	/* * * * *
	 * FINDONE
	 * * * * */
	public FreePost findById(UUID id) {
		try {
			return freePostRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Registro não encontrado com ID informado!");
		}
	}

	/* * * * *
	 * FINDALL
	 * * * * */

	public Page<FreePost> findAllByPage(String title, Pageable pageRequest) {
		if (!title.equals(""))
			return freePostRepository.findByTitleContaining(title, pageRequest);
		return freePostRepository.findAll(pageRequest);
	}

	public List<FreePost> findAll() {
		return freePostRepository.findAll();
	}

	/* * * * *
	 * DELETE
	 * * * * */
	public void delete(UUID id) {
		try {
			freePostRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Registro não encontrado com ID informado!");
		} catch (DataIntegrityViolationException e) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Esse registro não pode ser deletado!");
		}
	}

	/******************************************************************************/
	private void updateData(FreePost existingFreePost, FreePostDTO freePostDTO) {
		FreePost freePost = freePostDTO.toEntity();

		existingFreePost.setTitle(freePost.getTitle());
		existingFreePost.setDescription(freePost.getDescription());
		existingFreePost.setStatus(freePost.getStatus());
		existingFreePost.setUpdatedAt(freePost.getUpdatedAt());
		existingFreePost.setUpdatedBy(freePost.getUpdatedBy());
	}

	private void updateHistoric(HistoricStatusFreePost historicStatusFreePost, FreePostDTO freePostDTO) {
		Date date = new Date();
		
		//historicStatusFreePost.setFreePostId(freePost.getId());
		historicStatusFreePost.setStatus(freePostDTO.getStatus());
		historicStatusFreePost.setUpdatedAt(date);
		historicStatusFreePost.setUpdatedBy(freePostDTO.getUser());
	}

}
