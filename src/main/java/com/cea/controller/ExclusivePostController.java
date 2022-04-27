package com.cea.controller;

import com.cea.dto.exclusivePost.CreateContentDTO;
import com.cea.dto.exclusivePost.CreateSurveyDTO;
import com.cea.dto.exclusivePost.PageExclusivePostDTO;
import com.cea.models.ExclusivePost;
import com.cea.services.ExclusivePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/exclusive-posts")
@RequiredArgsConstructor
public class ExclusivePostController extends BasicController {

    private final ExclusivePostService exclusivePostService;

    @PostMapping("/content")
    public ResponseEntity createContent(
            @RequestBody @Valid CreateContentDTO payload) {
        this.exclusivePostService.createContent(payload);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/survey")
    public ResponseEntity createSurvey(
            @RequestBody @Valid CreateSurveyDTO payload) {
        this.exclusivePostService.createSurvey(payload);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/")
    public ResponseEntity<Page<ExclusivePost>> findAllPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "createdAt") String orderBy,
            @RequestParam(value = "type", defaultValue = "") String type,
            @RequestParam(value = "title", defaultValue = "") String title,
            @RequestParam(value = "status", defaultValue = "all") String status) {
        Pageable pageRequest = PageRequest.of(
                page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        Page<ExclusivePost> exclusivePosts = this.exclusivePostService.findAllByPage(type, title, status, pageRequest);

        return ResponseEntity.ok().body(exclusivePosts);
    }

    @GetMapping("/content")
    public ResponseEntity<PageExclusivePostDTO> findAllPageWithRelation(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "createdAt") String orderBy) {
        Pageable pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        PageExclusivePostDTO exclusivePosts = this.exclusivePostService.findAllExclusivePostWithMediaOrPollTopics(pageRequest);

        return ResponseEntity.ok(exclusivePosts);
    }

}
