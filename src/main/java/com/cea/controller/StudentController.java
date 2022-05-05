package com.cea.controller;

import com.cea.dto.students.IsStudentDTO;
import com.cea.dto.createAndUpdatePassword.RegisterPasswordDTO;
import com.cea.dto.createAndUpdatePassword.UpdatePasswordDTO;
import com.cea.dto.resetPassword.ResponseValidateTokenDTO;
import com.cea.dto.resetPassword.ValidateTokenDTO;
import com.cea.dto.students.StudentSocialNameDTO;
import com.cea.dto.students.StudentUploadDTO;
import com.cea.models.Student;
import com.cea.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        ResponseValidateTokenDTO validateToken = this.studentService.validateTokenForPasswordReset(
                payload, false);

        return ResponseEntity.ok().body(validateToken);
    }

    @PostMapping("/password/create")
    public ResponseEntity registerPassword(
            @RequestBody @Valid RegisterPasswordDTO payload) {
        this.studentService.registerPassword(payload);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/password/update")
    public void updatePassword(
            @RequestBody @Valid UpdatePasswordDTO payload) {
        this.studentService.updatePassword(payload);
    }

}
