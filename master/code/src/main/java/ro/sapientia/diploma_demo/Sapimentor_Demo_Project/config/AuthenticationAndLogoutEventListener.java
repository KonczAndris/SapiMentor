package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import javax.servlet.http.HttpSession;

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
        User user = userRepository.findByEmail(email);
        user.setStatus(1);
        userRepository.save(user);

        session.setAttribute("userEmail", email);
    }

    private void handleLogoutSuccess(LogoutSuccessEvent event) {
        session.removeAttribute("userEmail");
        String email = ((UserDetails) event.getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email);
        user.setStatus(0);
        userRepository.save(user);


    }

}
