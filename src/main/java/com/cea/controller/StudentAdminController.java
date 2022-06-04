package com.cea.controller;

import com.cea.dto.students.ResponsePageStudentsDTO;
import com.cea.dto.students.StudentSocialNameDTO;
import com.cea.dto.students.StudentUploadDTO;
import com.cea.models.Student;
import com.cea.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/students")
@RequiredArgsConstructor
@Validated
public class StudentAdminController {

    private final StudentService studentService;

    @GetMapping("/")
    public ResponseEntity<ResponsePageStudentsDTO> findAllByPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "10") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "status", defaultValue = "all") String status) {
        Pageable pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        ResponsePageStudentsDTO students = this.studentService.findAllByPage(name, status, pageRequest);
        return ResponseEntity.ok().body(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> findById(@PathVariable("id") UUID id) {
        Student student = this.studentService.findById(id);

        return ResponseEntity.ok(student);
    }

    @PatchMapping("/{id}")
    public ResponseEntity updateSocialName(
            @PathVariable("id") UUID id,
            @RequestBody @Valid StudentSocialNameDTO payload) {
        this.studentService.updateSocialName(id, payload);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<List<StudentUploadDTO>> importStudents(@RequestParam("file") MultipartFile file) {
        List<StudentUploadDTO> operations = this.studentService.importStudents(file);

        return ResponseEntity.ok(operations);
    }
}
