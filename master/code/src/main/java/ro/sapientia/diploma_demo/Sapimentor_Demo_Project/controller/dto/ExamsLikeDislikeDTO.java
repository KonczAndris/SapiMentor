package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamsLikeDislikeDTO {
    private Long id;
    private Long user_id;
    private Long exam_id;
    private Integer like;
    private Integer dislike;

    public ExamsLikeDislikeDTO(Long id,
                               Long user_id,
                               Long exam_id,
                               Integer like,
                               Integer dislike) {
        this.id = id;
        this.user_id = user_id;
        this.exam_id = exam_id;
        this.like = like;
        this.dislike = dislike;
    }

    public ExamsLikeDislikeDTO(Long id,
                               Integer like,
                               Integer dislike) {
        this.id = id;
        this.like = like;
        this.dislike = dislike;
    }
}
