package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.view.RedirectView;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import javax.servlet.http.HttpSession;

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

        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setStatus(0);
            userRepository.save(user);
        }
        session.removeAttribute("userEmail");

        // Redirect létrehozása a /login?expiredSession útvonalra
        RedirectView redirectView = new RedirectView("/login?expiredSession", true);

        // ModelAndView létrehozása a redirectView-vel
        ModelAndView modelAndView = new ModelAndView(redirectView);

        // Visszatérés a ModelAndView objektummal, ami elindítja a redirect-et
        try {
            throw new ModelAndViewDefiningException(modelAndView);
        } catch (ModelAndViewDefiningException e) {
            throw new RuntimeException(e);
        }
    }
}
