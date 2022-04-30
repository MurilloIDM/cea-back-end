package com.cea.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_student")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String socialName;
    @JsonIgnore
    private String password;
    private String email;
    private String phoneNumber;
    @Column(columnDefinition = "boolean default true")
    private boolean status;
    @Column(columnDefinition = "boolean default false")
    private boolean inactivationSoon;
    private LocalDateTime expirationDate;
    private LocalDateTime updatedAt;

    public static LocalDateTime getExpirationDate(String datePaymentStr, LocalDateTime dateNow) {
        String TIME_ZONE = "America/Sao_Paulo";

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime datePayment = LocalDateTime.parse(datePaymentStr, dateFormatter);

        LocalDateTime expirationDate = null;
        if (datePayment != null && dateNow != null) {
            expirationDate = LocalDateTime.from(datePayment.plusYears(1).atZone(ZoneId.of(TIME_ZONE)));

            boolean isActive = studentIsActive(expirationDate, dateNow);
            if (!isActive) {
                expirationDate = null;
            }
        }

        return expirationDate;
    }

    public static boolean studentIsActive(LocalDateTime expirationDate, LocalDateTime dateNow) {
        return dateNow.isBefore(expirationDate);
    }

    public String getFirstName() {
        String name = this.socialName != null ? this.socialName : this.name;

        if (name == null || name == "") {
            return "estudante";
        }

        String firstName = name.split(" ")[0];
        return firstName;
    }

    public String encryptPassword(String password) {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return passwordEncoder.encode(password);
    }

}
