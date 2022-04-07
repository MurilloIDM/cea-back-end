package com.cea.dto.externalPlatform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDataClientDTO {

    private String date_payment;
    private String sale_status_name;
    private String client_name;
    private String client_email;
    private String client_cel;

}

