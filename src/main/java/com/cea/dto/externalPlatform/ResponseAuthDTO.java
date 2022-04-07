package com.cea.dto.externalPlatform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAuthDTO {

    private Boolean success;
    private ResponseDataAuthDTO data;
    private ResponseProfileAuthDTO profile;

}
