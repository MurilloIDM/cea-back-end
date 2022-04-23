package com.cea.controller;

import com.cea.dto.exclusivePost.CreateContentDTO;
import com.cea.dto.exclusivePost.CreateSurveyDTO;
import com.cea.services.ExclusivePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/exclusive-post")
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

}
