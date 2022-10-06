package com.cea.services;

import com.cea.models.Link;
import com.cea.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;

    public List<Link> findByExclusivePostId(UUID exclusivePostId) {
        List<Link> links = this.linkRepository.findByExclusivePost_Id(exclusivePostId);

        if (links.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.NO_CONTENT, "");
        }

        return links;
    }

}
