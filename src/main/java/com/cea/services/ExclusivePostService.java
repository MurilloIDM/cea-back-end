package com.cea.services;

import com.cea.dto.exclusivePost.*;
import com.cea.dto.pollTopics.PollTopicsPercentageDTO;
import com.cea.enums.TypeExclusivePost;
import com.cea.models.ExclusivePost;
import com.cea.models.Media;
import com.cea.models.PollTopics;
import com.cea.repository.ExclusivePostRepository;
import com.cea.repository.MediaRepository;
import com.cea.repository.PollTopicsRepository;
import com.cea.utils.LocalDateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExclusivePostService {

    private final ExclusivePostRepository exclusivePostRepository;
    private final MediaRepository mediaRepository;
    private final PollTopicsRepository pollTopicsRepository;
    private final PollTopicsService pollTopicsService;
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

    public Page<ExclusivePost> findAllByPage(String type, String title, String status, Pageable pageRequest) {
        Boolean statusPost = null;

        if (status.equalsIgnoreCase("active") || status.equalsIgnoreCase("inative")) {
            statusPost = getStatusPost(status);
        }

        if (!type.equals("") && !title.equals("")) {
            if (!type.equalsIgnoreCase("TEXT") && !type.equalsIgnoreCase("SURVEY")) {
                throw new HttpClientErrorException(
                        HttpStatus.BAD_REQUEST, "O tipo não corresponde ao de enquete ou conteúdo!");
            }

            if (statusPost != null) {
                return this.exclusivePostRepository
                        .findByTitleIgnoreCaseContainingAndTypeEqualsAndStatusIsAndFiledFalse(
                            title, TypeExclusivePost.valueOf(type.toUpperCase()), statusPost, pageRequest);
            }

            return this.exclusivePostRepository.findByTitleIgnoreCaseContainingAndTypeEqualsAndFiledFalse(
                    title, TypeExclusivePost.valueOf(type.toUpperCase()), pageRequest);
        }

        if (!type.equals("") && title.equals("")) {
            if (!type.equalsIgnoreCase("TEXT") && !type.equalsIgnoreCase("SURVEY")) {
                throw new HttpClientErrorException(
                        HttpStatus.BAD_REQUEST, "O tipo não corresponde ao de enquete ou conteúdo!");
            }

            if (statusPost != null) {
                return this.exclusivePostRepository.findByTypeEqualsAndStatusIsAndFiledFalse(
                        TypeExclusivePost.valueOf(type.toUpperCase()), statusPost, pageRequest);
            }

            return this.exclusivePostRepository.findByTypeEqualsAndFiledFalse(
                    TypeExclusivePost.valueOf(type.toUpperCase()), pageRequest);
        }

        if (type.equals("") && !title.equals("")) {
            if (statusPost != null) {
                return this.exclusivePostRepository.findByTitleIgnoreCaseContainingAndStatusIsAndFiledFalse(
                        title, statusPost, pageRequest);
            }

            return this.exclusivePostRepository.findByTitleIgnoreCaseContainingAndFiledFalse(title, pageRequest);
        }

        if (statusPost != null) {
            return this.exclusivePostRepository.findAllByStatusIsAndFiledFalse(statusPost, pageRequest);
        }

        return this.exclusivePostRepository.findAllByFiledFalse(pageRequest);
    }

    public PageExclusivePostDTO findAllExclusivePostWithMediaOrPollTopics(Pageable pageRequest) {
        Page<ExclusivePost> exclusivePostsInPage = this.exclusivePostRepository
                .findAllByStatusTrueAndFiledFalse(pageRequest);
        List<ExclusivePost> postsInPage = exclusivePostsInPage.getContent();

        if (postsInPage.size() == 0) {
            throw new HttpClientErrorException(HttpStatus.NO_CONTENT, "");
        }

        List<ExclusivePostWithMediaOrPollTopicsDTO> posts = new ArrayList<>();
        for (ExclusivePost post : postsInPage) {
            UUID id = post.getId();
            TypeExclusivePost typePost = post.getType();

            ExclusivePostWithMediaOrPollTopicsDTO exclusivePost = ExclusivePostWithMediaOrPollTopicsDTO.toDTO(post);

            if (typePost.name().equals("TEXT")) {
                List<Media> media = this.mediaRepository.findByExclusivePost_Id(id);
                exclusivePost.setMedia(media);
            }

            if (typePost.name().equals("SURVEY")) {
                List<PollTopics> pollTopics = this.pollTopicsRepository.findByExclusivePost_Id(id);

                int totalVotes = this.pollTopicsService.getTotalVotes(pollTopics);
                List<PollTopicsPercentageDTO> pollTopicsWithPercentage = this.pollTopicsService
                        .getPollTopicsWithPercentage(pollTopics, totalVotes);

                exclusivePost.setPollTopics(pollTopicsWithPercentage);
            }

            posts.add(exclusivePost);
        }

        int size = exclusivePostsInPage.getSize();
        int totalPages = exclusivePostsInPage.getTotalPages();
        long totalElements = exclusivePostsInPage.getTotalElements();
        PageExclusivePostDTO exclusivePosts = new PageExclusivePostDTO(size, totalPages, totalElements, posts);

        return exclusivePosts;
    }

    private Boolean getStatusPost(String statusValue) {
        return (statusValue.equalsIgnoreCase("active")) ? true : false;
    }

}
