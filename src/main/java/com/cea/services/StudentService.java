package com.cea.services;

import com.cea.dto.IsStudentDTO;
import com.cea.dto.externalPlatform.ResponseDataClientDTO;
import com.cea.dto.externalPlatform.StudentInPlatformDTO;
import com.cea.models.Lead;
import com.cea.models.Student;
import com.cea.models.StudentTokens;
import com.cea.utils.LocalDateTimeUtils;
import com.cea.provider.ExternalPlatform;
import com.cea.provider.SendGridMail;
import com.cea.repository.LeadRepository;
import com.cea.repository.StudentRepository;
import com.cea.repository.StudentTokensRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final LeadRepository leadRepository;
    private final StudentTokensRepository studentTokensRepository;
    private final ExternalPlatform externalPlatform;
    private final SendGridMail sendGridMail;
    private final LocalDateTimeUtils localDateTimeUtils;

    public IsStudentDTO getIsStudent(String email) {
        boolean isStudent = false;
        boolean isLead = false;
        boolean isPrimaryAccess = false;
        boolean isActive = false;

        LocalDateTime dateNow = this.localDateTimeUtils.dateNow();
        Optional<Student> studentInBase = Optional.ofNullable(this.studentRepository.findByEmail(email));

        if (studentInBase.isPresent()) {
            isStudent = true;

            if (studentInBase.get().getPassword() == null) {
                isPrimaryAccess = true;
            }

            boolean isActiveStudent = Student.studentIsActive(studentInBase.get().getExpirationDate(), dateNow);

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

            LocalDateTime expirationDate = studentInExternalPlatform.isStudent()
                    ? Student.getExpirationDate(studentInExternalPlatform.getStudent().getDate_payment(), dateNow)
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

    private void basicInsert(ResponseDataClientDTO data, LocalDateTime expirationDate) {
        LocalDateTime dateNow = this.localDateTimeUtils.dateNow();

        Student student = new Student();

        student.setName(data.getClient_name());
        student.setEmail(data.getClient_email());
        student.setPhoneNumber(data.getClient_cel());
        student.setExpirationDate(expirationDate);
        student.setUpdatedAt(dateNow);

        this.studentRepository.save(student);
    }

    public void sendMailForgotPassword(String mailTo) {
        LocalDateTime dateNow = this.localDateTimeUtils.dateNow();
        Optional<Student> student = Optional.ofNullable(this.studentRepository.findByEmail(mailTo));

        if (student.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Estudante não encontrado!");
        }

        boolean studentExpirationDateNull = student.get().getExpirationDate() == null;
        boolean studentIsActive = !studentExpirationDateNull && Student.studentIsActive(
                student.get().getExpirationDate(), dateNow);

        if (!studentIsActive) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    "Estudante com acesso expirado ou não iniciado!");
        }

        String token = StudentTokens.generateToken();
        LocalDateTime expiresDate = this.localDateTimeUtils.addMinutes(dateNow, 90);

        StudentTokens studentToken = new StudentTokens(null, token, expiresDate, student.get());
        this.studentTokensRepository.save(studentToken);

        String firstName = student.get().getFirstName();

        try {
            this.sendGridMail.sendMail(mailTo, firstName, token);
        } catch (IOException e) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Falha ao enviar e-mail!");
        }
    }

}
