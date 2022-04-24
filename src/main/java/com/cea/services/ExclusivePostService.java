package com.cea.services;

import com.cea.dto.exclusivePost.CreateContentDTO;
import com.cea.dto.exclusivePost.CreateSurveyDTO;
import com.cea.dto.exclusivePost.MediaContentDTO;
import com.cea.dto.exclusivePost.PollTopicsDTO;
import com.cea.enums.TypeExclusivePost;
import com.cea.models.ExclusivePost;
import com.cea.models.Media;
import com.cea.models.PollTopics;
import com.cea.repository.ExclusivePostRepository;
import com.cea.repository.MediaRepository;
import com.cea.repository.PollTopicsRepository;
import com.cea.utils.LocalDateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExclusivePostService {

    private final ExclusivePostRepository exclusivePostRepository;
    private final MediaRepository mediaRepository;
    private final PollTopicsRepository pollTopicsRepository;
    private final LocalDateTimeUtils localDateTimeUtils;

    public void createContent(CreateContentDTO payload) {
        LocalDateTime dateNow = this.localDateTimeUtils.dateNow();

        ExclusivePost exclusivePost = payload.toEntity();
        exclusivePost.setCreatedAt(dateNow);
        exclusivePost.setUpdatedAt(dateNow);
        exclusivePost.setCreatedBy(payload.getUsername());

        if (!payload.getType().equalsIgnoreCase("TEXT")) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "O tipo não corresponde ao de conteúdo!");
        }

        exclusivePost.setType(TypeExclusivePost.valueOf(payload.getType()));
        this.exclusivePostRepository.save(exclusivePost);

        for (MediaContentDTO mediaContent : payload.getMedia()) {
            Media media = new Media();
            media.setUrl(mediaContent.getUrl());
            media.setExclusivePost(exclusivePost);

            this.mediaRepository.save(media);
        }
    }

    public void createSurvey(CreateSurveyDTO payload) {
        LocalDateTime dateNow = this.localDateTimeUtils.dateNow();

        ExclusivePost exclusivePost = payload.toEntity();
        exclusivePost.setCreatedAt(dateNow);
        exclusivePost.setUpdatedAt(dateNow);
        exclusivePost.setCreatedBy(payload.getUsername());

        if (!payload.getType().equalsIgnoreCase("SURVEY")) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "O tipo não corresponde ao de enquete!");
        }

        if (payload.getPollTopics().size() == 0) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST, "Deve ser cadastrado ao menos um tópico para a enquete!");
        }

        if (payload.getPollTopics().size() > 5) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Deve haver no máximo 5 tópicos por enquete!");
        }

        exclusivePost.setType(TypeExclusivePost.valueOf(payload.getType()));
        this.exclusivePostRepository.save(exclusivePost);

        for (PollTopicsDTO pollTopic : payload.getPollTopics()) {
            PollTopics pollTopics = new PollTopics();
            pollTopics.setDescription(pollTopic.getDescription());
            pollTopics.setExclusivePost(exclusivePost);

            this.pollTopicsRepository.save(pollTopics);
        }
    }

}
