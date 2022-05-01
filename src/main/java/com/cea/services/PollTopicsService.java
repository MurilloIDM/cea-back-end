package com.cea.services;

import com.cea.dto.pollTopics.PollTopicsPercentageDTO;
import com.cea.dto.pollTopics.ResponsePollTopicsWithPercentageDTO;
import com.cea.models.PollTopics;
import com.cea.models.Student;
import com.cea.models.StudentVotes;
import com.cea.repository.PollTopicsRepository;
import com.cea.repository.StudentVotesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PollTopicsService {

    private final PollTopicsRepository pollTopicsRepository;
    private final StudentVotesRepository studentVotesRepository;
    private final StudentService studentService;

    public ResponsePollTopicsWithPercentageDTO findVotesWithPercentageByExclusivePostId(UUID exclusivePostId) {
        List<PollTopics> pollTopics = this.pollTopicsRepository.findByExclusivePost_Id(exclusivePostId);

        if (pollTopics.size() == 0) {
            throw new HttpClientErrorException(HttpStatus.NO_CONTENT, "");
        }

        int totalVotes = this.getTotalVotes(pollTopics);
        List<PollTopicsPercentageDTO> listPollTopics = this.getPollTopicsWithPercentage(
                pollTopics, null, totalVotes);

        ResponsePollTopicsWithPercentageDTO pollTopicsWithPercentage = new ResponsePollTopicsWithPercentageDTO();
        pollTopicsWithPercentage.setTotalVotes(totalVotes);
        pollTopicsWithPercentage.setPollTopics(listPollTopics);

        return pollTopicsWithPercentage;
    }

    public int getTotalVotes(List<PollTopics> pollTopics) {
        int totalVotes = 0;

        for (PollTopics pollTopic : pollTopics) {
            totalVotes += pollTopic.getTotalVotes();
        }

        return totalVotes;
    }

    public List<PollTopicsPercentageDTO> getPollTopicsWithPercentage(
            List<PollTopics> pollTopics,
            Student student,
            int totalVotes) {
        List<PollTopicsPercentageDTO> pollTopicsWithPercentage = new ArrayList<>();

        for (PollTopics pollTopic : pollTopics) {
            boolean hasVote = false;
            double roundedPercentage = 0.0;
            int votes = pollTopic.getTotalVotes();

            if (totalVotes != 0) {
                double percentage =  (votes * 100.0) / totalVotes;

                BigDecimal bigDecimal = new BigDecimal(percentage).setScale(2, RoundingMode.HALF_DOWN);
                roundedPercentage = bigDecimal.doubleValue();
            }

            if (student != null) {
                Optional<StudentVotes> studentVotes = Optional.ofNullable(this.studentVotesRepository
                        .findByStudentAndPollTopics(student, pollTopic));

                if (studentVotes.isPresent()) {
                    hasVote = true;
                }
            }

            pollTopicsWithPercentage.add(new PollTopicsPercentageDTO(
                    pollTopic.getId(),
                    pollTopic.getDescription(),
                    votes,
                    roundedPercentage,
                    hasVote));
        }

        return pollTopicsWithPercentage;
    }
}
