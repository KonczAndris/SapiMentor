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
@NoArgsConstructor
@Entity
@Table(name = "confirmation_token")
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String token;
    private Date expirationTime;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public ConfirmationToken(String token, User user) {
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = TokenExpirationTime.getTokenExpirationTime();
    }

    public ConfirmationToken(String token) {
        super();
        this.token = token;
        this.expirationTime = TokenExpirationTime.getTokenExpirationTime();
    }

    //ez a regi ez mar nem kell mert meg van az utility-ben
//    public Date getTokenExpirationTime() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(new Date().getTime());
//        //elvileg nem kell csak a mysql-ben irja be rosszul az idot
//        //calendar.add(Calendar.HOUR, EXPIRATION_HOUR_TIME);
//        calendar.add(Calendar.MINUTE, EXPIRATION_MINUTE_TIME);
//        return new Date(calendar.getTime().getTime());
//    }
}
