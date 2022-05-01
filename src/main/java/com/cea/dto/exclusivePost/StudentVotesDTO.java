package com.cea.dto.exclusivePost;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentVotesDTO {

    @NotNull(message = "Campo obrigatório! Não deve ser vazio.")
    private UUID studentId;
    @NotNull(message = "Campo obrigatório! Não deve ser vazio.")
    private UUID pollTopicsId;
    @NotNull(message = "Campo obrigatório! Não deve ser vazio.")
    private UUID exclusivePostId;

}
