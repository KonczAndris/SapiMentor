package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@Transactional
@Component
public class AuthenticationAndLogoutEventListener implements ApplicationListener<AbstractAuthenticationEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @Override
    public void onApplicationEvent(AbstractAuthenticationEvent event) {
        if (event instanceof AuthenticationSuccessEvent) {
            handleAuthenticationSuccessEvent((AuthenticationSuccessEvent) event);
        } else if (event instanceof LogoutSuccessEvent) {
            handleLogoutSuccess((LogoutSuccessEvent) event);
        }
    }

    private void handleAuthenticationSuccessEvent(AuthenticationSuccessEvent event) {
        String email = ((UserDetails) event.getAuthentication().getPrincipal()).getUsername();
        System.out.println("email1: " + email);
        updateUserStatusByEmail(email, 1);
        System.out.println("email2: " + email);
        session.setAttribute("userEmail", email);
    }

    private void handleLogoutSuccess(LogoutSuccessEvent event) {
        String email = ((UserDetails) event.getAuthentication().getPrincipal()).getUsername();
        System.out.println("email3: " + email);
        updateUserStatusByEmail(email, 0);
        System.out.println("email4: " + email);
        session.removeAttribute("userEmail");

    }


    public void updateUserStatusByEmail(String email, int status) {
        // Az e-mail cím alapján kérdezzük le a felhasználót
        Long userId = userRepository.findIdByEmail(email);
        System.out.println("userId: " + userId);
        // Ellenőrizzük, hogy találtunk-e felhasználót
        if (userId != null) {
            // Ha igen, akkor frissítjük a státuszát
            userRepository.updateUserStatusById(userId, status);
        } else {
            // Ha nem, akkor hibát dobunk
            throw new RuntimeException("Don't find user with this email address: " + email);
        }
    }

}