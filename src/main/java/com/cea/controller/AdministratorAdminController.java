package com.cea.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import com.cea.dto.administrator.AdministratorCreatePasswordDTO;
import com.cea.dto.administrator.AdministratorUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cea.dto.administrator.AdministratorDTO;
import com.cea.dto.administrator.AdministratorResponseDTO;
import com.cea.models.Administrator;
import com.cea.services.AdministratorService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/administrators")
@RequiredArgsConstructor
public class AdministratorAdminController extends BasicController {

	private final AdministratorService administratorService;
	
	@PostMapping("/")
	public ResponseEntity<AdministratorResponseDTO> insert(@RequestBody AdministratorDTO administratorDTO) {
		AdministratorResponseDTO administrator = this.administratorService.insert(administratorDTO);
		
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(administrator.getId()).toUri();
		
		return ResponseEntity.created(uri).body(administrator);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Administrator> update(
		@PathVariable UUID id,
		@RequestBody AdministratorDTO administratorDTO
	) {
		this.administratorService.update(id, administratorDTO);
		
		return ResponseEntity.ok(null);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Administrator> findById(@PathVariable UUID id) {
		Administrator administrator = this.administratorService.findById(id);
		
		return ResponseEntity.ok(administrator);
	}
	
	@GetMapping("/")
	public ResponseEntity<Page<Administrator>> findAllByPage(
		@RequestParam(value = "page", defaultValue = "0") Integer page,
		@RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
		@RequestParam(value = "direction", defaultValue = "ASC") String direction,
		@RequestParam(value = "orderBy", defaultValue = "createdAt") String orderBy,
		@RequestParam(value = "name", defaultValue = "") String name
	) {
		Pageable pageRequest = PageRequest.of(
			page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		Page<Administrator> administrators = this.administratorService.findAllByPage(name, pageRequest);
		
		return ResponseEntity.ok().body(administrators);
	}

	@GetMapping("/all")
	public ResponseEntity<List<Administrator>> findAll() {
		List<Administrator> administrators = this.administratorService.findAll();
		
		return ResponseEntity.ok().body(administrators);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Administrator> delete(@PathVariable UUID id) {
		this.administratorService.delete(id);
		
		return ResponseEntity.ok(null);
	}

	@PostMapping("/password/generate")
	public ResponseEntity<AdministratorResponseDTO> generateTemporaryPassword(
			@RequestBody @Valid AdministratorUserDTO payload) {
		AdministratorResponseDTO administrator = this.administratorService.generateTempPassword(payload);

		return ResponseEntity.ok(administrator);
	}

	@PostMapping("/password/create")
	public ResponseEntity createPassword(
			@RequestBody @Valid AdministratorCreatePasswordDTO payload) {
		this.administratorService.createPassword(payload);

		return ResponseEntity.ok().build();
	}
}
