package com.cea.services;

import com.cea.dto.exclusivePost.*;
import com.cea.dto.pollTopics.PollTopicsPercentageDTO;
import com.cea.enums.TypeExclusivePost;
import com.cea.models.*;
import com.cea.repository.*;
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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExclusivePostService {

    private final ExclusivePostRepository exclusivePostRepository;
    private final MediaRepository mediaRepository;
    private final PollTopicsRepository pollTopicsRepository;
    private final StudentRepository studentRepository;
    private final StudentVotesRepository studentVotesRepository;
    private final PollTopicsService pollTopicsService;
    private final StudentService studentService;
    private final LocalDateTimeUtils localDateTimeUtils;

    public void createContent(ContentDTO payload) {
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

    public void createSurvey(SurveyDTO payload) {
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

        if (payload.getPollTopics().size() < 2 || payload.getPollTopics().size() > 5) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST, "Deve haver no mínimo 2 e no máximo 5 tópicos por enquete!");
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

    public PageExclusivePostDTO findAllExclusivePostWithMediaOrPollTopics(UUID studentId, Pageable pageRequest) {
        Page<ExclusivePost> exclusivePostsInPage = this.exclusivePostRepository
                .findAllByStatusTrueAndFiledFalse(pageRequest);
        List<ExclusivePost> postsInPage = exclusivePostsInPage.getContent();

        if (postsInPage.size() == 0) {
            throw new HttpClientErrorException(HttpStatus.NO_CONTENT, "");
        }

        Student student = this.studentService.findById(studentId);

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
                        .getPollTopicsWithPercentage(pollTopics, student, totalVotes);

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

    public void updateContent(UUID id, ContentDTO payload) {
        Optional<ExclusivePost> exclusivePost = this.exclusivePostRepository.findById(id);

        if (exclusivePost.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Conteúdo não encontrado!");
        }

        if (!payload.getType().equalsIgnoreCase("TEXT")) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "O tipo não corresponde ao de conteúdo!");
        }

        updateContentData(exclusivePost.get(), payload);

        this.exclusivePostRepository.save(exclusivePost.get());

        for (MediaContentDTO mediaContent : payload.getMedia()) {
            if (mediaContent.isRemove()) {
                this.mediaRepository.deleteById(mediaContent.getId());
                continue;
            }

            Media media = new Media();
            media.setId(mediaContent.getId());
            media.setUrl(mediaContent.getUrl());
            media.setExclusivePost(exclusivePost.get());

            this.mediaRepository.save(media);
        }
    }

    public void updateSurvey(UUID id, SurveyDTO payload) {
        Optional<ExclusivePost> exclusivePost = this.exclusivePostRepository.findById(id);

        if (exclusivePost.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Enquete não encontrada!");
        }

        if (!payload.getType().equalsIgnoreCase("SURVEY")) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "O tipo não corresponde ao de enquete!");
        }

        if (payload.getPollTopics().size() < 2 || payload.getPollTopics().size() > 5) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST, "Deve haver no mínimo 2 e no máximo 5 tópicos por enquete!");
        }

        updateSurveyData(exclusivePost.get(), payload);

        this.exclusivePostRepository.save(exclusivePost.get());

        List<PollTopics> existingPollTopics = this.pollTopicsRepository.findByExclusivePost_Id(id);
        int totalVotes = this.pollTopicsService.getTotalVotes(existingPollTopics);

        if (totalVotes != 0) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    "Enquete que já possui votos não pode ter tópicos atualizados ou adicionados!");
        }

        for (PollTopicsDTO pollTopic : payload.getPollTopics()) {
            if (pollTopic.isRemove()) {
                this.pollTopicsRepository.deleteById(pollTopic.getId());
                continue;
            }

            PollTopics pollTopics = new PollTopics();
            pollTopics.setId(pollTopic.getId());
            pollTopics.setDescription(pollTopic.getDescription());
            pollTopics.setExclusivePost(exclusivePost.get());

            this.pollTopicsRepository.save(pollTopics);
        }
    }
    
    public ExclusivePostWithMediaOrPollTopicsDTO addVotes(StudentVotesDTO payload) {
        UUID studentId = payload.getStudentId();
        UUID pollTopicsId = payload.getPollTopicsId();
        UUID exclusivePostId = payload.getExclusivePostId();

        Student student = this.studentService.findById(studentId);

        Optional<ExclusivePost> exclusivePost = this.exclusivePostRepository.findById(exclusivePostId);

        if (exclusivePost.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Enquete não encontrado!");
        }

        Optional<PollTopics> pollTopic = this.pollTopicsRepository.findById(pollTopicsId);

        if (pollTopic.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Tópico de enquete não encontrado!");
        }

        if (!pollTopic.get().getExclusivePost().getId().equals(exclusivePostId)) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST, "Tópico de enquete não pertence a enquete informada!");
        }

        List<StudentVotes> studentVotes = this.studentVotesRepository
                .findAllByExclusivePostAndStudent(exclusivePost.get(), student);

        if (studentVotes.size() > 0) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Estudante já tem voto nessa enquete!");
        }

        int votes = pollTopic.get().getTotalVotes() + 1;
        pollTopic.get().setTotalVotes(votes);

        this.pollTopicsRepository.save(pollTopic.get());

        StudentVotes studentVote = new StudentVotes();
        studentVote.setStudent(student);
        studentVote.setPollTopics(pollTopic.get());
        studentVote.setExclusivePost(exclusivePost.get());

        this.studentVotesRepository.save(studentVote);

        ExclusivePostWithMediaOrPollTopicsDTO response = ExclusivePostWithMediaOrPollTopicsDTO
                .toDTO(exclusivePost.get());

        List<PollTopics> pollTopicsInPost = this.pollTopicsRepository.findByExclusivePost_Id(exclusivePostId);
        int totalVotes = this.pollTopicsService.getTotalVotes(pollTopicsInPost);

        List<PollTopicsPercentageDTO> pollTopics = this.pollTopicsService.getPollTopicsWithPercentage(
                pollTopicsInPost, student, totalVotes);

        response.setPollTopics(pollTopics);

        return response;
    }

    private Boolean getStatusPost(String statusValue) {
        return (statusValue.equalsIgnoreCase("active")) ? true : false;
    }

    private void updateContentData(ExclusivePost exclusivePostAlreadyExists, ContentDTO contentDTO) {
        LocalDateTime dateNow = this.localDateTimeUtils.dateNow();

        ExclusivePost exclusivePost = contentDTO.toEntity();
        exclusivePostAlreadyExists.setTitle(exclusivePost.getTitle());
        exclusivePostAlreadyExists.setDescription(exclusivePost.getDescription());
        exclusivePostAlreadyExists.setStatus(exclusivePost.isStatus());
        exclusivePostAlreadyExists.setUpdatedBy(exclusivePost.getUpdatedBy());
        exclusivePostAlreadyExists.setUpdatedAt(dateNow);
    }

    private void updateSurveyData(ExclusivePost exclusivePostAlreadyExists, SurveyDTO surveyDTO) {
        LocalDateTime dateNow = this.localDateTimeUtils.dateNow();

        ExclusivePost exclusivePost = surveyDTO.toEntity();
        exclusivePostAlreadyExists.setTitle(exclusivePost.getTitle());
        exclusivePostAlreadyExists.setDescription(exclusivePost.getDescription());
        exclusivePostAlreadyExists.setStatus(exclusivePost.isStatus());
        exclusivePostAlreadyExists.setUpdatedBy(exclusivePost.getUpdatedBy());
        exclusivePostAlreadyExists.setUpdatedAt(dateNow);
    }

}
