package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long ratedUserId;
    private int score;
    private String date;
    private String comment;

    public Rating() {
    }

    public Rating(Long userId, Long ratedUserId, int score, String date, String comment) {
        this.userId = userId;
        this.ratedUserId = ratedUserId;
        this.score = score;
        this.date = date;
        this.comment = comment;
    }
}
