package com.cea.services;

import com.cea.dto.IsStudentDTO;
import com.cea.dto.externalPlatform.ResponseDataClientDTO;
import com.cea.dto.externalPlatform.StudentInPlatformDTO;
import com.cea.models.Lead;
import com.cea.models.Student;
import com.cea.provider.ExternalPlatform;
import com.cea.repository.LeadRepository;
import com.cea.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final LeadRepository leadRepository;
    private final ExternalPlatform externalPlatform;

    public IsStudentDTO getIsStudent(String email) {
        boolean isStudent = false;
        boolean isLead = false;
        boolean isPrimaryAccess = false;
        boolean isActive = false;

        Optional<Student> studentInBase = Optional.ofNullable(this.studentRepository.findByEmail(email));

        if (studentInBase.isPresent()) {
            isStudent = true;

            if (studentInBase.get().getPassword() == null) {
                isPrimaryAccess = true;
            }

            boolean isActiveStudent = Student.studentIsActive(studentInBase.get().getExpirationDate(), new Date());

            if (isActiveStudent) {
                isActive = true;
            }

            if (!isActiveStudent) {
                Optional<Lead> studentLead = Optional.ofNullable(this.leadRepository.findByEmail(email));

                if (studentLead.isPresent()) {
                    isLead = true;
                }
            }
        } else {
            StudentInPlatformDTO studentInExternalPlatform = this.externalPlatform.isStudentInPlatform(email);

            Date expirationDate = studentInExternalPlatform.isStudent()
                    ? Student.getExpirationDate(studentInExternalPlatform.getStudent().getDate_payment())
                    : null;

            if (studentInExternalPlatform.isStudent() && expirationDate == null) {
                isStudent = true;
            }

            if (studentInExternalPlatform.isStudent() && expirationDate != null) {
                this.basicInsert(studentInExternalPlatform.getStudent(), expirationDate);

                isActive = true;
                isStudent = true;
                isPrimaryAccess = true;
            } else {
                Optional<Lead> studentLead = Optional.ofNullable(this.leadRepository.findByEmail(email));

                if (studentLead.isPresent()) {
                    isLead = true;
                }
            }
        }

        return new IsStudentDTO(isStudent, isLead, isPrimaryAccess, isActive);
    }

    private void basicInsert(ResponseDataClientDTO data, Date expirationDate) {
        Student student = new Student();

        student.setName(data.getClient_name());
        student.setEmail(data.getClient_email());
        student.setPhoneNumber(data.getClient_cel());
        student.setExpirationDate(expirationDate);
        student.setUpdatedAt(new Date());

        this.studentRepository.save(student);
    }

}
