package com.cea.dto.exclusivePost;

import com.cea.models.ExclusivePost;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentDTO {

    @NotBlank(message = "Campo obrigatório! Não deve ser vazio.")
    private String title;
    @NotBlank(message = "Campo obrigatório! Não deve ser vazio.")
    private String description;
    @NotNull()
    private boolean status;
    @NotBlank(message = "Campo obrigatório! Não deve ser vazio.")
    private String type;
    @NotBlank(message = "Campo obrigatório! Não deve ser vazio.")
    private String username;
    @Valid
    private List<MediaContentDTO> media;
    @Valid
    private List<LinkContentDTO> links;

    public ExclusivePost toEntity() {
        ExclusivePost exclusivePost = new ExclusivePost();

        exclusivePost.setTitle(this.title);
        exclusivePost.setDescription(this.description);
        exclusivePost.setStatus(this.status);
        exclusivePost.setUpdatedBy(this.username);

        return exclusivePost;
    }

}
