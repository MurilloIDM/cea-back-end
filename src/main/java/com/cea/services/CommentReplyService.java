package com.cea.services;

import com.cea.models.CommentReply;
import com.cea.repository.CommentReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentReplyService {

    private final CommentReplyRepository commentReplyRepository;

    public List<CommentReply> findByCommentId(UUID commentId) {
        List<CommentReply> commentReplyList = this.commentReplyRepository.findAllByComment_IdAndStatusTrue(commentId);

        return commentReplyList;
    }

    public void inativeCommentReplyOfComment(UUID commentId) {
        List<CommentReply> commentReplyList = this.findByCommentId(commentId);

        for (CommentReply commentReply : commentReplyList) {
            commentReply.setStatus(false);
            this.commentReplyRepository.save(commentReply);
        }
    }

}
