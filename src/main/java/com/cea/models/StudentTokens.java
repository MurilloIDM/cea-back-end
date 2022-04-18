package com.cea.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_student_tokens")
public class StudentTokens implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;
    private String token;
    private LocalDateTime expiresDate;

    @ManyToOne
    private Student student;

    public static String generateToken() {
        String token = "";
        Random random = new Random();

        for (int x = 0; x < 6; x++) {
            int number = random.nextInt(10);
            token += number;
        }

        return token;
    }
}
