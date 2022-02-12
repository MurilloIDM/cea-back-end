package com.cea.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.cea.dto.AdministratorDTO;
import com.cea.dto.AdministratorResponseDTO;
import com.cea.models.Administrator;
import com.cea.repository.AdministratorRepository;

@Service
public class AdministratorService {

	@Autowired
	private AdministratorRepository administratorRepository;
	
	public AdministratorResponseDTO insert(AdministratorDTO administratorDTO) {		
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
		
		String password = this.generatePassword();
		
		BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
		String passwordHash = bCrypt.encode(password);
		
		administrator.setPassword(passwordHash);
		
		Administrator resultInsert = this.administratorRepository.save(administrator);
		
		AdministratorResponseDTO response = new AdministratorResponseDTO(
			resultInsert.getId(),
			resultInsert.getUsername(),
			password
		);
		
		return response;
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
	
	private String generatePassword() {
		String[] CHARS = {
          "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
          "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
          "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2",
          "3", "4", "5", "6", "7", "8", "9"
		};
		
		Random random = new Random();
		StringBuilder passwordSalt = new StringBuilder();
		
		while (passwordSalt.length() < 16) {
			Integer position = (int) (random.nextFloat() * CHARS.length);
			passwordSalt.append(CHARS[position]);
		}
		
		String password = passwordSalt.toString();
		return password;
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
