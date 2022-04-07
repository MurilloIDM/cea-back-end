package com.cea.controller;

import com.cea.dto.IsStudentDTO;
import com.cea.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/isStudent")
    public ResponseEntity<IsStudentDTO> isStudent(@RequestParam(value = "email") String email) {
        IsStudentDTO isStudent = this.studentService.getIsStudent(email);
        return ResponseEntity.ok().body(isStudent);
    }

}
