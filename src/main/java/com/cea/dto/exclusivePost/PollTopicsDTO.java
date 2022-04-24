package com.cea.dto.exclusivePost;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollTopicsDTO {

    @NotBlank(message = "Campo obrigatório! Não deve ser vazio.")
    private String description;

}
