package com.cea.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReplyDTO {

    @NotBlank(message = "Campo obrigatório! Não deve ser vazio.")
    @Length(min = 1, max = 255, message = "O comentário deve possuir no mínimo 1 caracter e no máximo 255 caracteres.")
    private String text;
    @NotNull(message = "Campo obrigatório! Não deve ser vazio.")
    private UUID commentId;
    @NotNull(message = "Campo obrigatório! Não deve ser vazio.")
    private UUID userId;

}
