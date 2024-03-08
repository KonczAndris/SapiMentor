package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLikeAndDislikeData {
    private Long resourceId;
    private Long userId1;
    private int like;
    private int dislike;

    public UserLikeAndDislikeData(Long resourceId, Long userId1, int like, int dislike) {
        this.resourceId = resourceId;
        this.userId1 = userId1;
        this.like = like;
        this.dislike = dislike;
    }
}
