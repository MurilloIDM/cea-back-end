package com.cea.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AdministratorService {

	private final AdministratorRepository administratorRepository;
	
	public AdministratorResponseDTO insert(AdministratorDTO administratorDTO) {		
		Administrator administrator = administratorDTO.toEntity();
		
		Administrator administratorAlreadyExists = this
			.administratorRepository
			.findByUsername(administrator.getUsername());
		
		if (administratorAlreadyExists != null) {
			throw new HttpClientErrorException(
				HttpStatus.BAD_REQUEST,
				"Já existe um registro com esse nome de acesso!"
			);
		}
		
		String password = this.generatePassword();
		
		BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
		String passwordHash = bCrypt.encode(password);
		
		administrator.setPassword(passwordHash);
		
		Administrator resultInsert = this.administratorRepository.save(administrator);

		return new AdministratorResponseDTO(
			resultInsert.getId(),
			resultInsert.getUsername(),
			password
		);
	}
	
	public Administrator update(UUID id, AdministratorDTO administratorDTO) {
		Administrator administratorAlreadyExists = this.findById(id);
		
		updateData(administratorAlreadyExists, administratorDTO);
		
		Administrator administratorAlreadyExistsUsername = this
				.administratorRepository
				.findByUsername(administratorAlreadyExists.getUsername());
		
		boolean administratorExistsWithUsername = administratorAlreadyExistsUsername != null;
		
		if (administratorExistsWithUsername) {
			boolean equalsId = administratorAlreadyExistsUsername.getId().equals(id);
			
			if (!equalsId) {
				throw new HttpClientErrorException(
					HttpStatus.BAD_REQUEST,
					"Já existe um registro com esse nome de acesso."
				);
			}
		}

		return this.administratorRepository.save(administratorAlreadyExists);
	}
	
	public Administrator findById(UUID id) {
		try {
			return this.administratorRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new HttpClientErrorException(
				HttpStatus.BAD_REQUEST,
				"Registro não encontrado com ID informado!"
			);	
		}
	}
	
	public Page<Administrator> findAllByPage(String name, Pageable pageRequest) {
		if (!name.equals("")) {

			return this.administratorRepository
			.findByNameContaining(name, pageRequest);
		}

		return this.administratorRepository.findAll(pageRequest);
	}
	
	public List<Administrator> findAll() {

		return this.administratorRepository.findAll();
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
			int position = (int) (random.nextFloat() * CHARS.length);
			passwordSalt.append(CHARS[position]);
		}

		return passwordSalt.toString();
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
