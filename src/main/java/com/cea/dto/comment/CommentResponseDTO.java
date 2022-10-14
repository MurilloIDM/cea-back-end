package com.cea.dto.comment;

import com.cea.models.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {

    private UUID id;
    private String text;
    private LocalDateTime createdAt;
    private UUID authorId;
    private String authorName;
    private String socialName;
    private int totalCommentsReply;
    private boolean admin;

    public static CommentResponseDTO toDTO(Comment comment) {
        CommentResponseDTO commentResponse = new CommentResponseDTO();

        commentResponse.setId(comment.getId());
        commentResponse.setText(comment.getText());
        commentResponse.setCreatedAt(comment.getCreatedAt());

        if (comment.getStudent() != null) {
            commentResponse.setAuthorId(comment.getStudent().getId());
            commentResponse.setAuthorName(comment.getStudent().getName());
            commentResponse.setSocialName(comment.getStudent().getSocialName());
            commentResponse.setAdmin(false);
        } else {
            commentResponse.setAuthorId(comment.getAdministrator().getId());
            commentResponse.setAuthorName("Equipe CEA");
            commentResponse.setAdmin(true);
        }

        return commentResponse;
    }
}
