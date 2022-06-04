package com.cea.controller;

import java.net.URI;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cea.dto.leads.LeadDTO;
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

	@GetMapping("/{deviceId}")
	public boolean findByDeviceId (@PathVariable UUID deviceId){
		return leadService.findByDeviceId(deviceId);
	}

}
