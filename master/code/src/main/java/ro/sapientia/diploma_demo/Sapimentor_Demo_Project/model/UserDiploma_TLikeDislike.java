package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "user_diploma_like_dislike")
public class UserDiploma_TLikeDislike {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        @ManyToOne
        @JoinColumn(name = "diploma_id")
        private Diploma_Theses diplomaTheses;

        // itt ha 0 akkor inactive es ha 1 akkor active
        @Column(name = "diploma_like_status")
        private Integer like = 0;

        // itt ha 0 akkor inactive es ha 1 akkor active
        @Column(name = "diploma_dislike_status")
        private Integer dislike = 0;

        public UserDiploma_TLikeDislike() {

        }
}