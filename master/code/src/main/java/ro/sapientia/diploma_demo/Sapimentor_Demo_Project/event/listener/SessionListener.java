package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@Transactional
@Component
public class SessionListener implements ApplicationListener<HttpSessionDestroyedEvent> {

    @Autowired
    private UserRepository userRepository;


    @Override
    public void onApplicationEvent(HttpSessionDestroyedEvent event) {
        HttpSession session = event.getSession();
        if (event.getSession().getAttribute("userEmail") == null) {
            return;
        }

        System.out.println("Session destroyed1: " + session.getAttribute("userEmail"));
        String email = (String) session.getAttribute("userEmail");
        System.out.println("Session destroyed2: " + email);
        if (email == null) {
            return;
        }

        System.out.println("Session destroyed3: " + email);
        Long userId = userRepository.findIdByEmail(email);
        if (userId != null) {
            // Ha igen, akkor frissítjük a státuszát
            userRepository.updateUserStatusById(userId, 0);
        } else {
            // Ha nem, akkor hibát dobunk
            throw new RuntimeException("Don't find user with this email address: " + email);
        }
        System.out.println("Session destroyed4: " + email);
        session.removeAttribute("userEmail");

    }
}
