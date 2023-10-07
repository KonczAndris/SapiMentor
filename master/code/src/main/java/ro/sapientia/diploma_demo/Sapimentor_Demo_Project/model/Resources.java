package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "resources")
public class Resources {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String link;
    private String topic_name;
    private String user_name;
    @Column(name = "resource_like")
    private Integer like;
    @Column(name = "resource_dislike")
    private Integer dislike;

    public Resources(String name,
                     String link,
                     String topic_name,
                     String user_name,
                     Integer like,
                     Integer dislike) {
        this.name = name;
        this.link = link;
        this.topic_name = topic_name;
        this.user_name = user_name;
        this.like = like;
        this.dislike = dislike;
    }

    public Resources() {

    }

}
