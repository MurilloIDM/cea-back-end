package com.cea.controller;

import com.cea.dto.leads.ResponsePageLeadsDTO;
import com.cea.models.Lead;
import com.cea.services.LeadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/leads")
@RequiredArgsConstructor
public class LeadAdminController {

    private final LeadService leadService;

    @GetMapping("/all")
    public ResponseEntity<List<Lead>> findAll() {
        List<Lead> leads = this.leadService.findAll();
        return ResponseEntity.ok().body(leads);
    }

    @GetMapping("/")
    public ResponseEntity<ResponsePageLeadsDTO> findAllByPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "createdAt") String orderBy) {
        Pageable pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        ResponsePageLeadsDTO leads = this.leadService.findAllByPage(pageRequest);
        return ResponseEntity.ok().body(leads);
    }

}
