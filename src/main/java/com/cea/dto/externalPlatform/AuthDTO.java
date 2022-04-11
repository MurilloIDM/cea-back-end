package com.cea.dto.externalPlatform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthDTO {

    private String email;
    private String publickey;
    private String apikey;

}
