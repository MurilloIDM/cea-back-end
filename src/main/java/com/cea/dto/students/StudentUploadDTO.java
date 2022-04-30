package com.cea.dto.students;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentUploadDTO {

    private String email;
    private String message;
    private boolean success;

}
