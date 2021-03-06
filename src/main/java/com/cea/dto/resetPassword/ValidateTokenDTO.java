package com.cea.dto.resetPassword;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

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
    @Length(min = 6, max = 6, message = "O token não possui o tamanho correto!")
    private String token;

}
