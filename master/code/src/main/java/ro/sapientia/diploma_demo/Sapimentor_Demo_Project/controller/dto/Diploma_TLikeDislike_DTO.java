package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Diploma_TLikeDislike_DTO {
    private Long id;
    private Long user_id;
    private Long diploma_id;
    private Integer like;
    private Integer dislike;

    public Diploma_TLikeDislike_DTO(Long id, Long user_id, Long diploma_id, Integer like, Integer dislike) {
        this.id = id;
        this.user_id = user_id;
        this.diploma_id = diploma_id;
        this.like = like;
        this.dislike = dislike;
    }

    public Diploma_TLikeDislike_DTO(Long id, Integer like, Integer dislike) {
        this.id = id;
        this.like = like;
        this.dislike = dislike;
    }

}
