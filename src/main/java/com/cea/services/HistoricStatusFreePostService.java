package com.cea.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cea.models.HistoricStatusFreePost;
import com.cea.repository.HistoricStatusFreePostRepository;

@Service
public class HistoricStatusFreePostService {

	@Autowired
	HistoricStatusFreePostRepository historicStatusFreePostRepository;

	public HistoricStatusFreePost insert(HistoricStatusFreePost historicStatusFreePost) {

		return historicStatusFreePostRepository.save(historicStatusFreePost);

	}

}
