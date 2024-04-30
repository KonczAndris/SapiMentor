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

    @Column(name = "exampdf")
    @Lob
    private byte[] examPDF;

    private String topic_name;

    private String user_name;

    @Column(name = "exam_like")
    private Integer like;

    @Column(name = "exam_dislike")
    private Integer dislike;

    private Long user_id;

    public Exams(String name,
                 byte[] examImage,
                 String topic_name,
                 String user_name,
                 Integer like,
                 Integer dislike,
                 Long user_id) {
        this.name = name;
        this.examImage = examImage;
        this.topic_name = topic_name;
        this.user_name = user_name;
        this.like = like;
        this.dislike = dislike;
        this.user_id = user_id;
    }

    public Exams(Long id,
                 Integer like,
                 Integer dislike) {
        this.id = id;
        this.like = like;
        this.dislike = dislike;
    }

    public Exams() {

    }
}
