package com.cea.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cea.models.FreePost;
import com.cea.services.FreePostService;

@RestController
@RequestMapping("/freeposts")
public class FreepostController {

	@Autowired
	FreePostService freePostService;

	@GetMapping("/")
	public List<FreePost> findAll() {
		return freePostService.findAll();
	}

	@GetMapping("/{id}")
	public FreePost findOne(@PathVariable UUID id) {
		return freePostService.findOne(id);
	}

	@PostMapping("/")
	public FreePost insert(@RequestBody FreePost freepost) {
		return freePostService.insert(freepost);
	}

	@PutMapping("/{id}")
	public FreePost update(@PathVariable UUID id, @RequestBody FreePost freepost) {
		return freePostService.update(id, freepost);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable UUID id) {
		freePostService.delete(id);
	}

}
