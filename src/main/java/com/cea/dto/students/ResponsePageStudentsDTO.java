package com.cea.dto.students;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePageStudentsDTO {

    private int size;
    private int totalPages;
    private long totalElements;
    private List<ResponseStudentDTO> content;

}
