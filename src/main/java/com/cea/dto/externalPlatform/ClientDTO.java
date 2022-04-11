package com.cea.dto.externalPlatform;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {

    private Boolean success;
    private List<ResponseDataClientDTO> data;

}
