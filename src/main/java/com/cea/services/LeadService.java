package com.cea.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.cea.dto.leads.ResponseLeadDTO;
import com.cea.dto.leads.ResponsePageLeadsDTO;
import com.cea.utils.LocalDateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.cea.dto.leads.LeadDTO;
import com.cea.models.Lead;
import com.cea.repository.LeadRepository;

@Service
@RequiredArgsConstructor
public class LeadService {

	private final LeadRepository leadRepository;
	private final LocalDateTimeUtils localDateTimeUtils;

	public Lead insert(LeadDTO leadDTO) {
		LocalDateTime dateNow = this.localDateTimeUtils.dateNow();

		Lead lead = leadDTO.toEntity();
		lead.setCreatedAt(dateNow);

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

	public ResponsePageLeadsDTO findAllByPage(Pageable pageRequest) {
		Page resultConsultPages = leadRepository.findAll(pageRequest);
		List<Lead> contentConsult = resultConsultPages.getContent();

		List<ResponseLeadDTO> contentLeadsDTO = new ArrayList<>();
		for (Lead lead : contentConsult) {
			ResponseLeadDTO responseLeadDTO = new ResponseLeadDTO();
			responseLeadDTO.setId(lead.getId());
			responseLeadDTO.setName(lead.getName());
			responseLeadDTO.setEmail(lead.getEmail());
			responseLeadDTO.setPhone(lead.getPhone());
			responseLeadDTO.setCreatedAt(lead.getCreatedAt());

			contentLeadsDTO.add(responseLeadDTO);
		}


		int size = resultConsultPages.getSize();
		int totalPages = resultConsultPages.getTotalPages();
		long totalElements = resultConsultPages.getTotalElements();
		ResponsePageLeadsDTO response = new ResponsePageLeadsDTO(size, totalPages, totalElements, contentLeadsDTO);

		return response;
	}
	
	public List<Lead> findAll() {
		return this.leadRepository.findAll();
	}

	public boolean findByDeviceId(UUID deviceId) {
		Lead lead = leadRepository.findByDeviceId(deviceId);
		
		if (lead == null) {
			return false;
		}

		return lead.getDeviceId() != null;
	}

}
