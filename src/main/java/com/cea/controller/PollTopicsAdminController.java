package com.cea.controller;

import com.cea.dto.pollTopics.ResponsePollTopicsWithPercentageDTO;
import com.cea.services.PollTopicsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/admin/poll-topics")
@RequiredArgsConstructor
public class PollTopicsAdminController extends BasicController {

    private final PollTopicsService pollTopicsService;

    @GetMapping("/exclusive-post/{exclusive_post_id}")
    public ResponseEntity<ResponsePollTopicsWithPercentageDTO> findVotesWithPercentageByExclusivePostId(
            @PathVariable("exclusive_post_id") UUID exclusivePostId) {
        ResponsePollTopicsWithPercentageDTO pollTopics = this.pollTopicsService
                .findVotesWithPercentageByExclusivePostId(exclusivePostId);

        return ResponseEntity.ok(pollTopics);
    }

}
