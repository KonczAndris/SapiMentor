package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.event.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.event.RegistrationCompleteEvent;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.UserService;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener
        implements ApplicationListener<RegistrationCompleteEvent> {

    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent registrationCompleteEvent) {
        //Get the newly registered user
        User theUser = registrationCompleteEvent.getUser();
        System.out.println("User:" + theUser);

        //Generate a verification token
        String verificationToken = UUID.randomUUID().toString();
        System.out.println("Token:" + verificationToken);

        //Save the verification token
        userService.saveUserVerificationToken(theUser, verificationToken);

        //Build the verification URL
        String Url = registrationCompleteEvent.getAppUrl()
                + "/register/verifyEmail?token=" + verificationToken;

        //Send the verification email
        log.info("Click on the link below to verify your email address :  {}", Url);
    }
}
