package com.cea.dto.leads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseLeadDTO {

    private UUID id;
    private String name;
    private String phone;
    private String email;
    private LocalDateTime createdAt;

}
