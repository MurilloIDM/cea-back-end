package com.cea.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenAdminDTO {

    private String accessToken;
    private boolean isPrimaryAccess;

}
