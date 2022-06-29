package com.cea.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenAdminDTO {

    private UUID userId;
    private String accessToken;
    private boolean isPrimaryAccess;

}
