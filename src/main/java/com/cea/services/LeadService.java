package com.cea.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cea.models.Lead;
import com.cea.repository.LeadRepository;

@Service
public class LeadService {

	@Autowired
	LeadRepository leadRepository;
	
	public Lead insert(Lead lead) {
		// TODO: Alterar implementações para utilizar DTO
		Date date = new Date();
		
		lead.setCreatedAt(date);
		lead.setUpdatedAt(date);
		
		return leadRepository.save(lead);
		
	}
	
}
