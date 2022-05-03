package com.cea.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReplyInativeDTO {

    @NotNull(message = "Campo obrigatório! Não deve ser vazio.")
    private UUID commentReplyId;
    @NotNull(message = "Campo obrigatório! Não deve ser vazio.")
    private UUID userId;

}
