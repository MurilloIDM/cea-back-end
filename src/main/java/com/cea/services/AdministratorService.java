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

import com.cea.dto.AdministratorDTO;
import com.cea.models.Administrator;
import com.cea.repository.AdministratorRepository;

@Service
public class AdministratorService {

	@Autowired
	private AdministratorRepository administratorRepository;
	
	public Administrator insert(AdministratorDTO administratorDTO) {		
		Administrator administrator = administratorDTO.toEntity();
		
		Administrator administratorAlreadyExists = this
			.administratorRepository
			.findByUsername(administrator.getUsername());
		
		if (administratorAlreadyExists != null) {
			throw new HttpClientErrorException(
				HttpStatus.BAD_REQUEST,
				"Já existe um registro com esse username!"
			);
		}
		
		return this.administratorRepository.save(administrator);
	}
	
	public Administrator update(UUID id, AdministratorDTO administratorDTO) {
		Administrator administratorAlreadyExists = this.findById(id);
		
		updateData(administratorAlreadyExists, administratorDTO);
		
		Administrator administratorAlreadyExistsUsername = this
				.administratorRepository
				.findByUsername(administratorAlreadyExists.getUsername());
		
		Boolean administratorExistsWithUsername = administratorAlreadyExistsUsername != null;
		
		if (administratorExistsWithUsername) {
			Boolean isNotEqualsId = administratorAlreadyExistsUsername.getId() != id;
			if (isNotEqualsId) {
				throw new HttpClientErrorException(
					HttpStatus.BAD_REQUEST,
					"Já existe um registro com esse username!"
				);
			}
		}
		
		Administrator administrator = this.administratorRepository.save(administratorAlreadyExists);
		
		return administrator;
	}
	
	public Administrator findById(UUID id) {
		try {
			Administrator administrator = this.administratorRepository.findById(id).get();			
			return administrator;
		} catch (NoSuchElementException e) {
			throw new HttpClientErrorException(
				HttpStatus.BAD_REQUEST,
				"Registro não encontrado com ID informado!"
			);	
		}
	}
	
	public Page<Administrator> findAllByPage(String name, Pageable pageRequest) {
		if (!name.equals("")) {
			Page<Administrator> administrators = this.administratorRepository
			.findByNameContaining(name, pageRequest);
			
			return administrators;
		}
		
		Page<Administrator> administrators = this.administratorRepository.findAll(pageRequest);
		
		return administrators;
	}
	
	public List<Administrator> findAll() {
		List<Administrator> administrators = this.administratorRepository.findAll();
		
		return administrators;
	}
	
	public void delete(UUID id) {
		try {
			this.administratorRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new HttpClientErrorException(
				HttpStatus.BAD_REQUEST,
				"Registro não encontrado com ID informado!"
			);
		} catch (DataIntegrityViolationException e) {
			throw new HttpClientErrorException(
				HttpStatus.INTERNAL_SERVER_ERROR,
				"Esse registro não pode ser deletado!"
			);
		}
	}
	
	private void updateData(
		Administrator administratorAlreadyExists,
		AdministratorDTO administratorDTO
	) {
		Administrator administrator = administratorDTO.toEntity();
		
		administratorAlreadyExists.setName(administrator.getName());
		administratorAlreadyExists.setUsername(administrator.getUsername());
		administratorAlreadyExists.setPassword(administrator.getPassword());
		administratorAlreadyExists.setUpdatedAt(administrator.getUpdatedAt());
		administratorAlreadyExists.setUpdatedBy(administrator.getUpdatedBy());
	}
	
}
