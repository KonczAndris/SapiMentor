package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Profile_Topics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String topic;

    @ElementCollection
    private List<String> tags = new ArrayList<>();

    // Visszafelé irányított referencia,
    // hogy ne legyen végtelen ciklus
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
