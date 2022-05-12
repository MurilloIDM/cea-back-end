package com.cea.controller;

import com.cea.models.Media;
import com.cea.services.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/media")
@RequiredArgsConstructor
public class MediaAdminController extends BasicController {

    private final MediaService mediaService;

    @GetMapping("/exclusive-post/{exclusive_post_id}")
    public ResponseEntity<List<Media>> findMediaByExclusivePostId(
            @PathVariable("exclusive_post_id") UUID exclusivePostId) {
        List<Media> media = this.mediaService.findByExclusivePostId(exclusivePostId);
        return ResponseEntity.ok(media);
    }

}
