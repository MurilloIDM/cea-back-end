package com.cea.services;

import com.cea.dto.students.*;
import com.cea.dto.createAndUpdatePassword.RegisterPasswordDTO;
import com.cea.dto.createAndUpdatePassword.UpdatePasswordDTO;
import com.cea.dto.externalPlatform.ResponseDataClientDTO;
import com.cea.dto.externalPlatform.StudentInPlatformDTO;
import com.cea.dto.resetPassword.ResponseValidateTokenDTO;
import com.cea.dto.resetPassword.ValidateTokenDTO;
import com.cea.models.Lead;
import com.cea.models.Student;
import com.cea.models.StudentCSV;
import com.cea.models.StudentTokens;
import com.cea.utils.LocalDateTimeUtils;
import com.cea.provider.ExternalPlatform;
import com.cea.provider.SendGridMail;
import com.cea.repository.LeadRepository;
import com.cea.repository.StudentRepository;
import com.cea.repository.StudentTokensRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.*;

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

    public void sendMailForgotPassword(String mailTo) {
        LocalDateTime dateNow = this.localDateTimeUtils.dateNow();
        Optional<Student> student = Optional.ofNullable(this.studentRepository.findByEmail(mailTo));

        if (student.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Estudante não encontrado!");
        }

        boolean studentIsActive = this.validateStatusStudent(student.get());

        if (!studentIsActive) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    "Estudante com acesso expirado ou não iniciado!");
        }

        List<StudentTokens> studentTokens = this.studentTokensRepository
                .findByStudentOrderByExpiresDateDesc(student.get());

        String token = StudentTokens.generateToken();
        LocalDateTime expiresDate = this.localDateTimeUtils.addMinutes(dateNow, 90);

        if (studentTokens.size() >= 3) {
            long diffTime = this.localDateTimeUtils.diffDateInMinutes(
                    studentTokens.get(0).getExpiresDate(),
                    expiresDate
            );

            if (diffTime < 5) {
                throw new HttpClientErrorException(
                        HttpStatus.BAD_REQUEST,
                        "Nos últimos minutos foram realizados 3 envios de tokens de recuperação! Aguarde 5 " +
                                "minutos para solicitar um novo token."
                );
            }
        }

        StudentTokens studentToken = new StudentTokens(null, token, expiresDate, student.get());
        this.studentTokensRepository.save(studentToken);

        String firstName = student.get().getFirstName();

        try {
            this.sendGridMail.sendMail(mailTo, firstName, token);
        } catch (IOException e) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Falha ao enviar e-mail!");
        }
    }

    public ResponseValidateTokenDTO validateTokenForPasswordReset(ValidateTokenDTO payload, boolean withDelete) {
        String token = payload.getToken();
        String email = payload.getEmail();

        Optional<Student> student = Optional.ofNullable(this.studentRepository.findByEmail(email));

        if (student.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Estudante não encontrado!");
        }

        List<StudentTokens> studentTokens = this.studentTokensRepository
                .findByStudentOrderByExpiresDateDesc(student.get());

        if (studentTokens.size() == 0) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Token inválido!");
        }

        StudentTokens studentToken = studentTokens.get(0);
        LocalDateTime expiresDate = studentToken.getExpiresDate();

        LocalDateTime dateNow = localDateTimeUtils.dateNow();
        boolean isExpires = localDateTimeUtils.validateDateTime(expiresDate, dateNow);

        if (!isExpires) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Token expirado!");
        }

        if (!studentToken.getToken().equals(token)) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Token inválido!");
        }

        if (withDelete) {
            this.studentTokensRepository.deleteAll(studentTokens);
        }

        return new ResponseValidateTokenDTO(true);
    }

    public void registerPassword(RegisterPasswordDTO payload) {
        String email = payload.getEmail();
        String password = payload.getPassword();

        Optional<Student> student = Optional.ofNullable(this.studentRepository.findByEmail(email));

        if (student.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Estudante não encontrado!");
        }

        boolean studentIsActive = this.validateStatusStudent(student.get());

        if (!studentIsActive) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    "Estudante com acesso expirado ou não iniciado!");
        }

        if (student.get().getPassword() != null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Estudante já possui senha cadastrada!");
        }

        String encryptedPassword = student.get().encryptPassword(password);
        student.get().setPassword(encryptedPassword);

        this.studentRepository.save(student.get());
    }

    public void updatePassword(UpdatePasswordDTO payload) {
        String email = payload.getEmail();
        String token = payload.getToken();
        String password = payload.getPassword();

        Optional<Student> student = Optional.ofNullable(this.studentRepository.findByEmail(email));

        if (student.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Estudante não encontrado!");
        }

        boolean studentIsActive = this.validateStatusStudent(student.get());

        if (!studentIsActive) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    "Estudante com acesso expirado ou não iniciado!");
        }

        if (student.get().getPassword() == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Estudante não possui senha cadastrada!");
        }

        ResponseValidateTokenDTO validToken = this.validateTokenForPasswordReset(
                new ValidateTokenDTO(email, token),
                true
        );

        if (validToken.isValidToken()) {
            String encryptedPassword = student.get().encryptPassword(password);
            student.get().setPassword(encryptedPassword);

            this.studentRepository.save(student.get());
        }
    }

    public ResponsePageStudentsDTO findAllByPage(String name, String status, Pageable pageRequest) {
        Page<Student> studentsPage = null;

        if (!name.equals("")) {
            if (status.equalsIgnoreCase("active") || status.equalsIgnoreCase("inative")) {
                boolean statusValue = status.equalsIgnoreCase("active") ? true : false;

                studentsPage = this.studentRepository
                        .findAllByStatusIsAndNameIgnoreCaseContainingOrSocialNameIgnoreCaseContaining(
                                statusValue, name, name, pageRequest
                        );
            }


            if (status.equalsIgnoreCase("in_deactivation")) {
                studentsPage = this.studentRepository
                        .findAllByInactivationSoonTrueAndNameIgnoreCaseContainingOrSocialNameIgnoreCaseContaining(
                                name, name, pageRequest
                        );
            }

            if (status.equalsIgnoreCase("all")) {
                studentsPage = this.studentRepository
                        .findAllByNameIgnoreCaseContainingOrSocialNameIgnoreCaseContaining(name, name, pageRequest);
            }
        }

        if (name.equals("")) {
            if (status.equalsIgnoreCase("active") || status.equalsIgnoreCase("inative")) {
                boolean statusValue = status.equalsIgnoreCase("active") ? true : false;

                studentsPage = this.studentRepository.findAllByStatusIs(statusValue, pageRequest);
            }

            if (status.equalsIgnoreCase("in_deactivation")) {
                studentsPage = this.studentRepository.findAllByInactivationSoonIsTrue(pageRequest);
            }

            if (status.equalsIgnoreCase("all")) {
                studentsPage = this.studentRepository.findAll(pageRequest);
            }
        }

        List<Student> contentInPage = studentsPage.getContent();

        List<ResponseStudentDTO> contentResponse = new ArrayList<>();
        for (Student student : contentInPage) {
            ResponseStudentDTO responseStudentDTO = new ResponseStudentDTO();
            responseStudentDTO.setId(student.getId());
            responseStudentDTO.setName(student.getName());
            responseStudentDTO.setEmail(student.getEmail());
            responseStudentDTO.setStatus(student.isStatus());
            responseStudentDTO.setSocialName(student.getSocialName());
            responseStudentDTO.setExpirationDate(student.getExpirationDate());
            responseStudentDTO.setInactivationSoon(student.isInactivationSoon());

            contentResponse.add(responseStudentDTO);
        }

        int size = studentsPage.getSize();
        int totalPages = studentsPage.getTotalPages();
        long totalElements = studentsPage.getTotalElements();
        ResponsePageStudentsDTO response = new ResponsePageStudentsDTO(size, totalPages, totalElements, contentResponse);

        return response;
    }

    public Student findById(UUID id) {
        Optional<Student> student = this.studentRepository.findById(id);

        if (student.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Estudante não encontrado!");
        }

        return student.get();
    }

    public void updateSocialName(UUID id, StudentSocialNameDTO payload) {
        Optional<Student> student = this.studentRepository.findById(id);

        if (student.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Estudante não encontrado!");
        }

        LocalDateTime dateNow = this.localDateTimeUtils.dateNow();

        String socialName = payload.getSocialName();
        student.get().setSocialName(socialName);
        student.get().setUpdatedAt(dateNow);

        this.studentRepository.save(student.get());
    }
    
    public List<StudentUploadDTO> importStudents(MultipartFile file) {
        if (file.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "O arquivo de upload está vazio!");
        }

        List<StudentCSV> studentsCSV = null;
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            CsvToBean<StudentCSV> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(StudentCSV.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            studentsCSV = csvToBean.parse();

        } catch (Exception ex) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Falha ao processar arquivo!");
        }

        List<StudentUploadDTO> operations = new ArrayList<>();

        for (StudentCSV studentCSV : studentsCSV) {
            String name = studentCSV.getName().trim();
            String email = studentCSV.getEmail().trim();
            String dateStr = studentCSV.getExpirationDate().trim();
            String phoneNumber = studentCSV.getPhoneNumber().trim();

            Optional<Student> studentAlreadyExists = Optional.ofNullable(this.studentRepository.findByEmail(email));

            StudentUploadDTO studentUpload = new StudentUploadDTO();

            if (studentAlreadyExists.isPresent()) {
                studentUpload.setEmail(email);
                studentUpload.setSuccess(false);
                studentUpload.setMessage("Já existe estudante cadastrado com o e-mail informado!");
                operations.add(studentUpload);
                continue;
            }

            if (name.equals("") || email.equals("") || dateStr.equals("")) {
                studentUpload.setEmail(email);
                studentUpload.setSuccess(false);
                studentUpload.setMessage("Não foi fornecido nome, email e data de expiração!");
                operations.add(studentUpload);
                continue;
            }

            LocalDateTime dateNow = this.localDateTimeUtils.dateNow();
            LocalDateTime expirationDate = this.localDateTimeUtils.convertStringToDate(dateStr);

            boolean isActive = Student.studentIsActive(expirationDate, dateNow);
            boolean inactivationSoon = Student.withInactivationSoon(expirationDate, dateNow);

            Student student = new Student();
            student.setName(name);
            student.setEmail(email);
            student.setStatus(isActive);
            student.setUpdatedAt(dateNow);
            student.setPhoneNumber(phoneNumber);
            student.setExpirationDate(expirationDate);
            student.setInactivationSoon(inactivationSoon);

            this.studentRepository.save(student);

            studentUpload.setEmail(email);
            studentUpload.setSuccess(true);
            studentUpload.setMessage("Estudante cadastrado com sucesso!");

            operations.add(studentUpload);
        }

        return operations;
    }

    private void basicInsert(ResponseDataClientDTO data, LocalDateTime expirationDate) {
        LocalDateTime dateNow = this.localDateTimeUtils.dateNow();

        Student student = new Student();

        student.setName(data.getClient_name());
        student.setEmail(data.getClient_email());
        student.setPhoneNumber(data.getClient_cel());
        student.setExpirationDate(expirationDate);
        student.setStatus(true);
        student.setUpdatedAt(dateNow);

        this.studentRepository.save(student);
    }

    private boolean validateStatusStudent(Student student) {
        LocalDateTime dateNow = this.localDateTimeUtils.dateNow();

        boolean studentExpirationDateNull = student.getExpirationDate() == null;
        boolean studentIsActive = !studentExpirationDateNull && Student.studentIsActive(
                student.getExpirationDate(), dateNow);

        return studentIsActive;
    }

}
