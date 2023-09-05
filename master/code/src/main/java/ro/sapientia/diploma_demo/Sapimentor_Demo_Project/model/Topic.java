package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String topic;
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL)
    private List<Skill> skills;

    public Topic(String topic) {
        this.topic = topic;
        this.skills = new ArrayList<>();
    }

    public Topic() {

    }


}
