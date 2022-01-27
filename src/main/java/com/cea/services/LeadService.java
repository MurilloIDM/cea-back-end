package com.cea.services;

import java.util.List;
import java.util.UUID;

import org.hibernate.type.BinaryType;
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
		Lead checkDeviceId = this.leadRepository.findByDeviceId(leadDTO.getDeviceId());
		
		if (checkEmail != null && checkPhone != null && checkDeviceId == null) {
			Lead leadUpdated = this.leadRepository.getById(checkEmail.getId());
			leadUpdated.setDeviceId(leadDTO.getDeviceId());
			return this.leadRepository.save(leadUpdated);
		}

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

	public boolean findByDeviceId(UUID deviceId) {
		System.out.println(deviceId);
		Lead lead = leadRepository.findByDeviceId(deviceId);
		
		if (lead == null) {
			return false;
		}
	
		if (lead.getDeviceId() == null) {
			return false;	
		}
			
		return true;
	}

}
