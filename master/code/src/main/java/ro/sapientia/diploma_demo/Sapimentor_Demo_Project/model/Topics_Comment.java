package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Topics_Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String ratedTopicId;
    private String subject;
    private String comment;
    private String date;

    public Topics_Comment() {
    }

    public Topics_Comment(Long userId, String ratedTopicId, String subject, String comment, String date) {
        this.userId = userId;
        this.ratedTopicId = ratedTopicId;
        this.subject = subject;
        this.comment = comment;
        this.date = date;
    }
}
