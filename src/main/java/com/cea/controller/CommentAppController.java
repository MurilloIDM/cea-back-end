package com.cea.controller;

import com.cea.dto.comment.*;
import com.cea.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/app/comments")
@RequiredArgsConstructor
public class CommentAppController extends BasicController {

    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity addComment(@RequestBody @Valid CommentDTO payload) {
        this.commentService.addComment(payload);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/reply/create")
    public ResponseEntity addCommentReply(@RequestBody @Valid CommentReplyDTO payload) {
        this.commentService.addCommentReply(payload);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/reply")
    public ResponseEntity inativeCommentReply(
            @RequestBody @Valid CommentReplyInativeDTO payload) {
        this.commentService.inativeCommentReply(payload);

        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/")
    public ResponseEntity inativeComment(@RequestBody @Valid CommentInativeDTO payload) {
        this.commentService.inativeComment(payload);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/exclusive-post/{exclusivePostId}")
    public ResponseEntity<CommentsResponseDTO> findAllComments(
            @PathVariable("exclusivePostId") UUID exclusivePostId,
            @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Pageable pageRequest = PageRequest.of(page, 20, Sort.Direction.valueOf("DESC"), "createdAt");

        CommentsResponseDTO comments = this.commentService.findAllComments(exclusivePostId, pageRequest);

        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{commentId}/comments-reply")
    public ResponseEntity<CommentsReplyResponseDTO> findAllCommentsReply(
            @PathVariable("commentId") UUID commentId,
            @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Pageable pageRequest = PageRequest.of(page, 5, Sort.Direction.valueOf("DESC"), "createdAt");

        CommentsReplyResponseDTO commentsReply = this.commentService.findAllCommentsReply(commentId, pageRequest);

        return ResponseEntity.ok(commentsReply);
    }

}
