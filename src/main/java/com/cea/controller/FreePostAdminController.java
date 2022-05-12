package com.cea.controller;

import com.cea.dto.FreePostDTO;
import com.cea.models.FreePost;
import com.cea.models.HistoricStatusFreePost;
import com.cea.services.FreePostService;
import com.cea.services.HistoricStatusFreePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/admin/free-posts")
@RequiredArgsConstructor
public class FreePostAdminController {

    private final FreePostService freePostService;
    private final HistoricStatusFreePostService historicStatusFreePostService;

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
    public ResponseEntity<Page<FreePost>> findAllPaged(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "createdAt") String orderBy,
            @RequestParam(value = "title", defaultValue = "") String title,
            @RequestParam(value = "status", defaultValue = "") String status) {
        Pageable pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        Page<FreePost> freePosts = freePostService.findAllPaged(title, status, pageRequest);

        return ResponseEntity.ok().body(freePosts);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FreePost> delete(@PathVariable UUID id) {
        freePostService.delete(id);

        return ResponseEntity.ok(null);
    }

}
