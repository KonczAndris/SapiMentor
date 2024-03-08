package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendContactEmail {
    private final JavaMailSenderImpl contactMailSender;

    public void sendContactEmail(String name, String email, String message) {
        try {
            sendContactEmailDetails(name, email, message);
        } catch (Exception e) {
            log.error("Error sending contact email: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Async
    public void sendContactEmailDetails(String name, String email, String message)
            throws MessagingException, UnsupportedEncodingException{
        String subject = "Message from " + email;
        String senderName = "SapiMentor Contact Service";
        String mailContent = "<p> <b><i>Hi SapiMentor Information Reception Employee, </i></b></p>"+
                "<p><b>You got an email from the user :<br>" + email + ",</b>"+"" +
                "<br>Please read this message from the user carefully and try to <br>" +
                " respond as soon as possible to the best of your knowledge!.</p>"+
                "<p><b>The message:</b></p>"+
                "<p> <i>"+message+"</i> </p>"+
                "<p><b>Best regards, <br> SapiMentor Contact Service</b>";

        contactEmailMessage(subject, senderName, mailContent, contactMailSender, email);
    }

    private static void contactEmailMessage(String subject,
                                            String senderName,
                                            String mailContent,
                                            JavaMailSenderImpl mailSender,
                                            String email)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom(email, senderName);
        messageHelper.setTo("sapimentor@gmail.com");
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }

}
