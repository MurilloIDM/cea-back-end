package com.cea.dto.pollTopics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PollTopicsPercentageDTO {

    private UUID id;
    private String description;
    private int votes;
    private Double percentageVotes;
    private boolean hasVote;

}
