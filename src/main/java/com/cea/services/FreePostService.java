package com.cea.services;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class FreePostService {
	
	private final FreePostRepository freePostRepository;
	private final HistoricStatusFreePostService historicStatusFreePostService;

	public FreePost insert(FreePostDTO freePostDTO) {

		FreePost freePost = freePostDTO.toEntity();

		return freePostRepository.save(freePost);
	}

	public FreePost update(UUID id, FreePostDTO freePostDTO) {
		FreePost existingFreePost = findById(id);

		if (existingFreePost.getStatus() != freePostDTO.getStatus()) {
			HistoricStatusFreePost historicStatusFreePost = new HistoricStatusFreePost();
			updateHistoricDTO(historicStatusFreePost, freePostDTO);
			historicStatusFreePost.setFreePost(existingFreePost);
			historicStatusFreePostService.insert(historicStatusFreePost);
			existingFreePost.getHistoricStatusFreePost().add(historicStatusFreePost);
		}

		updateData(existingFreePost, freePostDTO);

		return freePostRepository.save(existingFreePost);
	}

	public FreePost findById(UUID id) {
		try {
			return freePostRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Registro não encontrado com ID informado!");
		}
	}

	public Page<FreePost> findAllPaged(String title, String status, Pageable pageRequest) {
		if (!title.equals("") && (status.equals("") || status.equals("all")))
			return freePostRepository.findByTitleContaining(title, pageRequest);

		if (!status.equals("") && !status.equals("all") && title.equals("")) {
			Boolean statusBoolean = status.equals("online") ? true : false;
			return freePostRepository.findByStatusIs(statusBoolean, pageRequest);
		}

		if (!status.equals("") && !status.equals("all") && !title.equals("")) {
			Boolean statusBoolean = status.equals("online") ? true : false;
			return freePostRepository.findByTitleContainingAndStatusIs(title, statusBoolean, pageRequest);
		}

		return freePostRepository.findAll(pageRequest);
	}

	public List<FreePost> findAll() {
		return freePostRepository.findByStatusTrue();
	}

	public void delete(UUID id) {
		FreePost freePost = findById(id);
		for (HistoricStatusFreePost historic : freePost.getHistoricStatusFreePost()) {
			historicStatusFreePostService.delete(historic.getId());
		}

		try {
			freePostRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Registro não encontrado com ID informado!");
		} catch (DataIntegrityViolationException e) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Esse registro não pode ser deletado!");
		}
	}

	private void updateData(FreePost existingFreePost, FreePostDTO freePostDTO) {
		FreePost freePost = freePostDTO.toEntity();

		existingFreePost.setTitle(freePost.getTitle());
		existingFreePost.setDescription(freePost.getDescription());
		existingFreePost.setImageUrl(freePost.getImageUrl());
		existingFreePost.setStatus(freePost.getStatus());
		existingFreePost.setUpdatedAt(freePost.getUpdatedAt());
		existingFreePost.setUpdatedBy(freePost.getUpdatedBy());
	}

	private void updateHistoricDTO(HistoricStatusFreePost historicStatusFreePost, FreePostDTO freePostDTO) {
		Date date = new Date();

		historicStatusFreePost.setStatus(freePostDTO.getStatus());
		historicStatusFreePost.setUpdatedAt(date);
		historicStatusFreePost.setUpdatedBy(freePostDTO.getUser());
	}

	public void updateHistoric(HistoricStatusFreePost historicStatusFreePost, FreePost freePost) {
		Date date = new Date();

		historicStatusFreePost.setFreePost(findById(freePost.getId()));
		historicStatusFreePost.setStatus(freePost.getStatus());
		historicStatusFreePost.setUpdatedAt(date);
		historicStatusFreePost.setUpdatedBy(freePost.getUpdatedBy());
	}

}
