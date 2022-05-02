package com.cea.controller;

import com.cea.dto.comment.CommentDTO;
import com.cea.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController extends BasicController {

    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity addComment(@RequestBody @Valid CommentDTO payload) {
        this.commentService.addComment(payload);

        return ResponseEntity.ok().build();
    }

}
