package com.cea.dto.exclusivePost;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollTopicsDTO {

    private UUID id;
    @NotBlank(message = "Campo obrigatório! Não deve ser vazio.")
    private String description;
    private boolean remove;

}
