package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "diploma_theses", uniqueConstraints = @UniqueConstraint(columnNames = "id"))
public class Diploma_Theses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "diploma_theses_name")
    private String name;

    @Column(name = "diploma_theses_file")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] diploma_theses_file;

    @Column(name = "diploma_theses_like")
    private Integer like;

    @Column(name = "diploma_theses_dislike")
    private Integer dislike;

    private String topic_name;

    private String user_name;

    private String year;

    public Diploma_Theses(String name,
                          byte[] diploma_theses_file,
                          String topic_name,
                          String user_name,
                          Integer like,
                          Integer dislike,
                          String year) {
        this.name = name;
        this.diploma_theses_file = diploma_theses_file;
        this.topic_name = topic_name;
        this.user_name = user_name;
        this.like = like;
        this.dislike = dislike;
        this.year = year;
    }

    public Diploma_Theses(Long id,
                          Integer like,
                          Integer dislike) {
        this.id = id;
        this.like = like;
        this.dislike = dislike;
    }

    public Diploma_Theses() {

    }

}
