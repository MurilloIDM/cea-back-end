package com.cea.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

	public List<Lead> findAll() {
		return leadRepository.findAll();
	}

	public Optional<Lead> findById(UUID id) {
		return leadRepository.findById(id);
	}

	public Lead update(UUID id, Lead lead) {
		lead.setId(id);
		return leadRepository.save(lead);
	}
	
}
