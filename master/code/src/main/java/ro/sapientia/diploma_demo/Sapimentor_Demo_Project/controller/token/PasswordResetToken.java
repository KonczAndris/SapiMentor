package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility.TokenExpirationTime;
import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "password_reset_token")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date exiprationTime;
    @OneToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.exiprationTime = TokenExpirationTime.getTokenExpirationTime();
        this.user = user;
    }
}
