package com.cea.dto.students;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseStudentDTO {

    private UUID id;
    private String name;
    private boolean status;
    private String socialName;
    private boolean inactivationSoon;
    private LocalDateTime expirationDate;

}
