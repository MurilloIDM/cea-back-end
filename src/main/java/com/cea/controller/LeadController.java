package com.cea.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cea.models.Lead;
import com.cea.services.LeadService;

@RestController
@RequestMapping("/leads")
public class LeadController {

	@Autowired
	LeadService leadService;

	/* Create */
	@PostMapping("/")
	public Lead insert(@RequestBody Lead lead) {
		
		String phone = lead.getPhone();
		String email = lead.getEmail();
		
		if (leadService.findByEmail(email) == null || leadService.findByPhone(phone) == null)
			return this.leadService.insert(lead);

		return lead;
}

	/* Find All */
	@GetMapping("/")
	public List<Lead> findAll() {
		return leadService.findAll();
	}

	/* Find One */
	@GetMapping("/{id}")
	public Optional<Lead> findById(@PathVariable UUID id) {
		return leadService.findById(id);
	}

	/* Update */
	@PutMapping("/{id}")
	public Lead update(@PathVariable UUID id, @RequestBody Lead lead) {
		return leadService.update(id, lead);
	}
}
