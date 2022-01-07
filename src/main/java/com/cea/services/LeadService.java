package com.cea.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.cea.dto.LeadDTO;
import com.cea.models.Lead;
import com.cea.repository.LeadRepository;

@Service
public class LeadService {

	@Autowired
	LeadRepository leadRepository;

	public Lead insert(LeadDTO leadDTO) {

		Lead lead = leadDTO.toEntity();
		Lead checkEmail = this.leadRepository.findByEmail(leadDTO.getEmail());
		Lead checkPhone = this.leadRepository.findByPhone(leadDTO.getPhone());

		if (checkEmail != null || checkPhone != null) {
			throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Email ou telefone já cadastrados");
		} else
			return this.leadRepository.save(lead);
	}

	public Page<Lead> findAllByPage(Pageable pageRequest) {
		Page<Lead> leads = leadRepository.findAll(pageRequest);
		return leads;
	}
	
	public List<Lead> findAll() {
		List<Lead> leads = this.leadRepository.findAll();
		
		return leads;
	}

	public void updateData(LeadDTO leadDTO) {

		Lead lead = leadDTO.toEntity();
		lead.setName(lead.getName());
		lead.setPhone(lead.getPhone());
		lead.setEmail(lead.getEmail());
		lead.setCreatedAt(lead.getCreatedAt());

	}
}
