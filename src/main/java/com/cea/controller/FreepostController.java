package com.cea.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/freeposts")
public class FreepostController {

	@Autowired
	FreePostService freePostService;

	@Autowired
	HistoricStatusFreePostService historicStatusFreePostService;

	@PostMapping("/")
	public ResponseEntity<FreePost> insert(@RequestBody FreePostDTO freepostDTO) {
		FreePost freePost = freePostService.insert(freepostDTO);
		HistoricStatusFreePost historicStatusFreePost = new HistoricStatusFreePost();
		freePostService.updateHistoric(historicStatusFreePost, freePost);
		historicStatusFreePostService.insert(historicStatusFreePost);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(freePost.getId())
				.toUri();

		return ResponseEntity.created(uri).body(null);
	}

	@PutMapping("/{id}")
	public ResponseEntity<FreePost> update(@PathVariable UUID id, @RequestBody FreePostDTO freepostDTO) {
		freePostService.update(id, freepostDTO);
		return ResponseEntity.ok(null);
	}

	@GetMapping("/{id}")
	public ResponseEntity<FreePost> findById(@PathVariable UUID id) {
		return ResponseEntity.ok(freePostService.findById(id));
	}

	@GetMapping("/")
	public ResponseEntity<Page<FreePost>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "createdAt") String orderBy,
			@RequestParam(value = "title", defaultValue = "") String title) {
		Pageable pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);

		Page<FreePost> freePosts = freePostService.findAllByPage(title, pageRequest);

		return ResponseEntity.ok().body(freePosts);
	}

	@GetMapping("/all")
	public ResponseEntity<List<FreePost>> findAll() {
		return ResponseEntity.ok().body(freePostService.findAll());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<FreePost> delete(@PathVariable UUID id) {
		freePostService.delete(id);

		return ResponseEntity.ok(null);
	}

}
