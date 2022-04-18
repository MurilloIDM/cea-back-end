package com.cea.controller;

import com.cea.dto.IsStudentDTO;
import com.cea.dto.resetPassword.ResponseValidateTokenDTO;
import com.cea.dto.resetPassword.ValidateTokenDTO;
import com.cea.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@Validated
public class StudentController extends BasicController {

    private final StudentService studentService;

    @GetMapping("/isStudent")
    public ResponseEntity<IsStudentDTO> isStudent(@RequestParam(value = "email") String email) {
        IsStudentDTO isStudent = this.studentService.getIsStudent(email);
        return ResponseEntity.ok().body(isStudent);
    }

    @PostMapping("/password/forgot")
    public void sendMailForgotPassword(@RequestParam(value = "email") Optional<String> mailParam) {
        String mailTo = mailParam.orElseThrow(() ->
                new HttpClientErrorException(HttpStatus.BAD_REQUEST, "O e-mail do estudante não foi informado!"));

        this.studentService.sendMailForgotPassword(mailTo);
    }

    @PostMapping("/password/reset/validate-token")
    public ResponseEntity<ResponseValidateTokenDTO> validateTokenForPasswordReset(
            @RequestBody @Valid ValidateTokenDTO payload) {
        ResponseValidateTokenDTO validateToken = this.studentService.validateTokenForPasswordReset(payload);

        return ResponseEntity.ok().body(validateToken);
    }

}
