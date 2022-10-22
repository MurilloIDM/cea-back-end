package com.cea.dto.createAndUpdatePassword;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPasswordDTO {

    @NotBlank(message = "Campo obrigatório! Não deve ser vazio.")
    @Email(message = "O campo não segue a formatação adequada para um e-mail.")
    private String email;
    @NotBlank(message = "Campo obrigatório! Não deve ser vazio.")
    @Pattern(
            regexp = "(?=^.{8,}$)((?=.*\\d)(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$",
            message = "A senha não está dentro do padrão! Deve conter letras minúsculas e maiúsculas," +
                    "números e caracteres especiais.")
    private String password;

}
