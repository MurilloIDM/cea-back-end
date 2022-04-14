package com.cea.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cea.dto.LeadDTO;
import com.cea.models.Lead;
import com.cea.services.LeadService;

@RestController
@RequestMapping("/leads")
@RequiredArgsConstructor
public class LeadController {

	private final LeadService leadService;

	@PostMapping("/")
	public ResponseEntity<Lead> insert(@RequestBody LeadDTO leadDTO) {
		Lead lead = this.leadService.insert(leadDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(lead.getId()).toUri();

		return ResponseEntity.created(uri).body(null);
	}

	@GetMapping("/all")
	public ResponseEntity<List<Lead>> findAll() {
		List<Lead> leads = this.leadService.findAll();
		return ResponseEntity.ok().body(leads);
	}

	@GetMapping("/")
	public ResponseEntity<Page<Lead>> findAllByPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "createdAt") String orderBy)
			{
		Pageable pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Page<Lead> leads = this.leadService.findAllByPage(pageRequest);
		return ResponseEntity.ok().body(leads);
	}

	@GetMapping("/{deviceId}")
	public boolean findByDeviceId (@PathVariable UUID deviceId){
		return leadService.findByDeviceId(deviceId);
	}

}
