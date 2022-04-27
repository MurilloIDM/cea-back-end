package com.cea.dto.pollTopics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePollTopicsWithPercentageDTO {

    private int totalVotes;
    private List<PollTopicsPercentageDTO> pollTopics;

}
