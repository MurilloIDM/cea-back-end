package com.cea.services;

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
		return freePostRepository.save(freePost);
	}

	/* * * * *
	 * UPDATE
	 * * * * */
	public FreePost update(UUID id, FreePostDTO freePostDTO) {
		FreePost existingFreePost = findById(id);

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
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,
					"Registro não encontrado com ID informado!");
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

}
