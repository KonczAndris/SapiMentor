package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "exams")
public class Exams {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "examname")
    private String name;
    @Column(name = "examimage")
    @Lob
    private byte[] examImage;
    private String topic_name;
    private String user_name;
    @Column(name = "exam_like")
    private Integer like;
    @Column(name = "exam_dislike")
    private Integer dislike;

    public Exams(String name,
                 byte[] examImage,
                 String topic_name,
                 String user_name,
                 Integer like,
                 Integer dislike) {
        this.name = name;
        this.examImage = examImage;
        this.topic_name = topic_name;
        this.user_name = user_name;
        this.like = like;
        this.dislike = dislike;
    }

    public Exams() {

    }
}
