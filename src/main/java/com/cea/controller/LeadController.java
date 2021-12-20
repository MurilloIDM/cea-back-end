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

import com.cea.models.UserLead;
import com.cea.services.LeadService;

@RestController
@RequestMapping("/leads")
public class LeadController {

	@Autowired
	LeadService leadService;
	
	/*Create*/
	@PostMapping("/")
	public UserLead insert(@RequestBody UserLead lead) {
		return this.leadService.insert(lead);
	}
	
	/*Find All*/
	@GetMapping("/")
	public List<UserLead> findAll(){
		return leadService.findAll();
	}
	
	/*Find One*/
	@GetMapping("/{id}")
	public Optional<UserLead> findById(@PathVariable UUID id){
		return leadService.findById(id);
	}
	
	/*Update*/
	@PutMapping("/{id}")
	public UserLead update(@PathVariable UUID id, @RequestBody UserLead lead) {
		return leadService.update(id, lead);
	}
}
