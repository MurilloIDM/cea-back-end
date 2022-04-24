package com.cea.dto.exclusivePost;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageExclusivePostDTO {

    private int size;
    private int totalPages;
    private long totalElements;
    private List<ExclusivePostWithMediaOrPollTopicsDTO> content;

}
