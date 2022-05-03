package com.cea.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsReplyResponseDTO {

    private int size;
    private int totalPages;
    private long totalElements;
    List<CommentReplyResponseDTO> content;

}
