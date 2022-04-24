package com.cea.services;

import com.cea.models.Media;
import com.cea.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepository;

    public List<Media> findByExclusivePostId(UUID exclusivePostId) {
        List<Media> media = this.mediaRepository.findByExclusivePost_Id(exclusivePostId);

        if (media.size() == 0) {
            throw new HttpClientErrorException(HttpStatus.NO_CONTENT, "");
        }

        return media;
    }

}
