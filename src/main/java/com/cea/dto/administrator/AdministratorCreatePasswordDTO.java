package com.cea.dto.administrator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdministratorCreatePasswordDTO {

    @NotBlank(message = "Campo obrigatório! Não deve ser vazio.")
    private String username;
    @NotBlank(message = "Campo obrigatório! Não deve ser vazio.")
    @Pattern(
            regexp = "(?=^.{8,}$)((?=.*\\d)(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$",
            message = "A senha não está dentro do padrão! Deve conter letras minúsculas e maiúsculas," +
                    "números e caracteres especiais.")
    private String password;

}
