package com.cea.dto.leads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePageLeadsDTO {

    private int size;
    private int totalPages;
    private long totalElements;
    private List<ResponseLeadDTO> content;

}
