package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinksLikeDislikeDTO {
    private Long id;
    private Long user_id;
    private Long resource_id;
    private Integer like;
    private Integer dislike;

    public LinksLikeDislikeDTO(Long id,
                               Long user_id,
                               Long resource_id,
                               Integer like,
                               Integer dislike) {
        this.id = id;
        this.user_id = user_id;
        this.resource_id = resource_id;
        this.like = like;
        this.dislike = dislike;
    }
}
