package com.cea.dto.externalPlatform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDataAuthDTO {

    private String token;
    private String token_valid_until;

}
