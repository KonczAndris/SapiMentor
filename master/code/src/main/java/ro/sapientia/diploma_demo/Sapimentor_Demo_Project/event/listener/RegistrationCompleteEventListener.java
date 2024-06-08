package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.event.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import lombok.var;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.event.RegistrationCompleteEvent;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.UserService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener
        implements ApplicationListener<RegistrationCompleteEvent> {

    private final UserService userService;
    private final JavaMailSenderImpl mailSender;
    private User user;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent registrationCompleteEvent) {
        User theUser = registrationCompleteEvent.getUser();
        this.user = theUser;
        //Generate a verification token
        String verificationToken = UUID.randomUUID().toString();

        //Save the verification token
        userService.saveUserVerificationToken(theUser, verificationToken);

        //Build the verification URL
        String Url = registrationCompleteEvent.getAppUrl()
                + "/register/verifyEmail?token=" + verificationToken;

        //Send the verification email
        try {
            sendVerificationEmail(Url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Click on the link below to verify your email address :  {}", Url);
    }

    @Async
    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Confirmation";
        String senderName = "SapiMentor Registration Portal Service";
        String mailContent = "<p> Hi "+ user.getFirst_Name()+ ", </p>"+
                "<p>Thank you for registering with us,"+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you, <br> SapiMentor Registration Portal Service";
        emailMessage(subject, senderName, mailContent, mailSender, user);
    }

    @Async
    public void sendPasswordResetVerificationEmail(User user,String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Password Reset";
        String senderName = "SapiMentor Forgot Password Portal Service";
        String mailContent = "<p> Hi "+ user.getFirst_Name()+ ", </p>"+
                "<p><b>You recently requested to reset your password,</b>"+"" +
                "<br>Please, follow the link below to complete the action.</p>"+
                "<a href=\"" +url+ "\">Reset password</a>"+
                "<p> Thank you, <br> SapiMentor Forgot Password Portal Service";
        emailMessage(subject, senderName, mailContent, mailSender, user);
    }


    private static void emailMessage(String subject, String senderName,
                                     String mailContent, JavaMailSender mailSender, User user)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("sapimentor@gmail.com", senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }


}
