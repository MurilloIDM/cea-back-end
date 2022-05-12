package com.cea.controller;

import com.cea.dto.exclusivePost.ExclusivePostWithMediaOrPollTopicsDTO;
import com.cea.dto.exclusivePost.PageExclusivePostDTO;
import com.cea.dto.exclusivePost.StudentVotesDTO;
import com.cea.services.ExclusivePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/app/exclusive-posts")
@RequiredArgsConstructor
public class ExclusivePostAppController {

    private final ExclusivePostService exclusivePostService;

    @GetMapping("/content")
    public ResponseEntity<PageExclusivePostDTO> findAllPageWithRelation(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "createdAt") String orderBy,
            @RequestParam(value = "studentId", defaultValue = "") UUID studentId) {
        Pageable pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        PageExclusivePostDTO exclusivePosts = this.exclusivePostService
                .findAllExclusivePostWithMediaOrPollTopics(studentId, pageRequest);

        return ResponseEntity.ok(exclusivePosts);
    }

    @PostMapping("/survey/vote")
    public ResponseEntity<ExclusivePostWithMediaOrPollTopicsDTO> addVoteSurvey(
            @RequestBody @Valid StudentVotesDTO payload) {
        ExclusivePostWithMediaOrPollTopicsDTO exclusivePost = this.exclusivePostService.addVotes(payload);

        return ResponseEntity.ok(exclusivePost);
    }

}
