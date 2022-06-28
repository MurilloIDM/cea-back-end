package com.cea.dto.comment;

import com.cea.models.Administrator;
import com.cea.models.Comment;
import com.cea.models.CommentReply;
import com.cea.models.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReplyResponseDTO {

    private UUID id;
    private String text;
    private LocalDateTime createdAt;
    private UUID authorId;
    private String authorName;
    private String socialName;

    public static CommentReplyResponseDTO toDTO(CommentReply commentReply) {
        CommentReplyResponseDTO commentReplyResponse = new CommentReplyResponseDTO();

        commentReplyResponse.setId(commentReply.getId());
        commentReplyResponse.setText(commentReply.getText());
        commentReplyResponse.setCreatedAt(commentReply.getCreatedAt());

        if (commentReply.getStudent() != null) {
            commentReplyResponse.setAuthorId(commentReply.getStudent().getId());
            commentReplyResponse.setAuthorName(commentReply.getStudent().getName());
            commentReplyResponse.setSocialName(commentReply.getStudent().getSocialName());
        } else {
            commentReplyResponse.setAuthorId(commentReply.getAdministrator().getId());
            commentReplyResponse.setAuthorName("Administrador");
        }

        return commentReplyResponse;
    }

}
