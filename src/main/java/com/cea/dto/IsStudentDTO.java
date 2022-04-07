package com.cea.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IsStudentDTO {

    private boolean student;
    private boolean lead;
    private boolean primaryAccess;
    private boolean isActive;

}
