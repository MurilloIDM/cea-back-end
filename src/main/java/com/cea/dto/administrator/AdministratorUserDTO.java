package com.cea.dto.administrator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministratorUserDTO {

    @NotBlank(message = "Campo obrigatório! Não deve ser vazio.")
    private String username;

}
