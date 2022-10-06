package com.cea.controller;

import com.cea.models.Link;
import com.cea.services.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/links")
@RequiredArgsConstructor
public class LinkAdminController extends BasicController {

    private final LinkService linkService;

    @GetMapping("/exclusive-post/{exclusive_post_id}")
    public ResponseEntity<List<Link>> findLinkByExclusivePostId(
            @PathVariable("exclusive_post_id")UUID exclusivePostId) {
        List<Link> links = this.linkService.findByExclusivePostId(exclusivePostId);

        return ResponseEntity.ok(links);
    }

}
