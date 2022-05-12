package com.cea.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cea.dto.FreePostDTO;
import com.cea.models.FreePost;
import com.cea.models.HistoricStatusFreePost;
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
