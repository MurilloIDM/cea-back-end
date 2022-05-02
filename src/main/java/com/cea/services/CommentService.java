package com.cea.services;

import com.cea.models.CommentReply;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentReply commentReply;

}
