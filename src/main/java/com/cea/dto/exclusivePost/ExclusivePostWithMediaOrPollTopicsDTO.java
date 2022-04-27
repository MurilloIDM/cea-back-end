package com.cea.dto.exclusivePost;

import com.cea.dto.pollTopics.PollTopicsPercentageDTO;
import com.cea.enums.TypeExclusivePost;
import com.cea.models.ExclusivePost;
import com.cea.models.Media;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExclusivePostWithMediaOrPollTopicsDTO {

    private UUID id;
    private TypeExclusivePost type;
    private String title;
    private String description;
    private boolean status;
    private boolean filed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private List<Media> media = new ArrayList<Media>();
    private List<PollTopicsPercentageDTO> pollTopics = new ArrayList<PollTopicsPercentageDTO>();

    public static ExclusivePostWithMediaOrPollTopicsDTO toDTO(ExclusivePost exclusivePost) {
       ExclusivePostWithMediaOrPollTopicsDTO exclusivePostDTO = new ExclusivePostWithMediaOrPollTopicsDTO();

       exclusivePostDTO.setId(exclusivePost.getId());
       exclusivePostDTO.setType(exclusivePost.getType());
       exclusivePostDTO.setTitle(exclusivePost.getTitle());
       exclusivePostDTO.setDescription(exclusivePost.getDescription());
       exclusivePostDTO.setStatus(exclusivePost.isStatus());
       exclusivePostDTO.setFiled(exclusivePost.isFiled());
       exclusivePostDTO.setCreatedAt(exclusivePost.getCreatedAt());
       exclusivePostDTO.setCreatedBy(exclusivePost.getCreatedBy());
       exclusivePostDTO.setUpdatedAt(exclusivePost.getUpdatedAt());
       exclusivePostDTO.setUpdatedBy(exclusivePost.getUpdatedBy());

       return exclusivePostDTO;
    }

}
