package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.token;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import javax.persistence.*;
import java.util.Calendar;
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

    private static final int EXPIRATION_MINUTE_TIME = 15;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public ConfirmationToken(String token, User user) {
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public ConfirmationToken(String token) {
        super();
        this.token = token;
        this.expirationTime = this.getTokenExpirationTime();
    }
    public Date getTokenExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        //elvileg nem kell csak a mysql-ben irja be rosszul az idot
        //calendar.add(Calendar.HOUR, EXPIRATION_HOUR_TIME);
        calendar.add(Calendar.MINUTE, EXPIRATION_MINUTE_TIME);
        return new Date(calendar.getTime().getTime());
    }
}
