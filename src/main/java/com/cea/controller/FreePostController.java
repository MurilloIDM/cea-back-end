package com.cea.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cea.models.FreePost;
import com.cea.services.FreePostService;
import com.cea.services.HistoricStatusFreePostService;

@RestController
@RequestMapping("/free-posts")
@RequiredArgsConstructor
public class FreePostController {

	private final FreePostService freePostService;
	private final HistoricStatusFreePostService historicStatusFreePostService;

	@GetMapping("/all")
	public ResponseEntity<List<FreePost>> findAll() {
		return ResponseEntity.ok().body(freePostService.findAll());
	}

}
