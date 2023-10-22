package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "user_resource_like_dislike")
public class UserResourceLikeDislike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    private Resources resources;

    // itt ha 0 akkor inactive es ha 1 akkor active
    @Column(name = "resource_like_status")
    private Integer like;

    // itt ha 0 akkor inactive es ha 1 akkor active
    @Column(name = "resource_dislike_status")
    private Integer dislike;

    public UserResourceLikeDislike() {

    }
}
