package com.cea.dto.resetPassword;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateTokenDTO {

    @NotBlank(message = "Campo obrigatório! Não deve ser vazio.")
    @Email(message = "O campo não segue a formatação adequada para um e-mail.")
    private String email;
    @NotBlank(message = "Campo obrigatório! Não deve ser vazio.")
    private String token;

}
