package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility;

import java.util.Calendar;
import java.util.Date;

public class TokenExpirationTime {
    private static final int EXPIRATION_MINUTE_TIME = 15;

    public static Date getTokenExpirationTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_MINUTE_TIME);
        return new Date(calendar.getTime().getTime());
    }
}
