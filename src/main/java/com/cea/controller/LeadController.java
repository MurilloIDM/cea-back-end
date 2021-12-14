package com.cea.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@PostMapping("/")
	public Lead insert(@RequestBody Lead lead) {
		return this.leadService.insert(lead);
	}
	
}
