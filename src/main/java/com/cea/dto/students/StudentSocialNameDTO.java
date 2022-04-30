package com.cea.dto.students;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentSocialNameDTO {

    @NotBlank(message = "Campo obrigatório! Não deve ser vazio.")
    private String socialName;
}
