package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "user_exam_like_dislike")
public class UserExamLikeDislike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exams exams;

    // itt ha 0 akkor inactive es ha 1 akkor active
    @Column(name = "exam_like_status")
    private Integer like = 0;

    // itt ha 0 akkor inactive es ha 1 akkor active
    @Column(name = "exam_dislike_status")
    private Integer dislike = 0;

    public UserExamLikeDislike() {

    }

    public UserExamLikeDislike(Long id,
                               Integer like,
                               Integer dislike) {
        this.id = id;
        this.like = like;
        this.dislike = dislike;
    }
}
