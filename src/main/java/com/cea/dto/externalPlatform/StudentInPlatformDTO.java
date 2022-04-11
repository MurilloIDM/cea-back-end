package com.cea.dto.externalPlatform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentInPlatformDTO {

    private boolean isStudent;
    private ResponseDataClientDTO student;

}
