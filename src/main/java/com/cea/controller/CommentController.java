package com.cea.controller;

import com.cea.dto.comment.CommentDTO;
import com.cea.dto.comment.CommentInativeDTO;
import com.cea.dto.comment.CommentReplyDTO;
import com.cea.dto.comment.CommentReplyInativeDTO;
import com.cea.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

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

    @PostMapping("/reply/create")
    public ResponseEntity addCommentReply(@RequestBody @Valid CommentReplyDTO payload) {
        this.commentService.addCommentReply(payload);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/reply")
    public ResponseEntity inativeCommentReply(
            @RequestBody @Valid CommentReplyInativeDTO payload) {
        this.commentService.inativeCommentReply(payload);
    }
    
    @PatchMapping("/")
    public ResponseEntity inativeComment(@RequestBody @Valid CommentInativeDTO payload) {
        this.commentService.inativeComment(payload);

        return ResponseEntity.ok().build();
    }

}
