package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config;

import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyCustomSessionInformationExpiredStrategy implements InvalidSessionStrategy, SessionInformationExpiredStrategy {


    private UserRepository userRepository;

    //    @Override
//    public void onExpiredSessionDetected(SessionInformationExpiredEvent event)
//            throws IOException, ServletException {
//        System.out.println("Session expired");
//        String email = event.getSessionInformation().getPrincipal().toString();
//        User user = userRepository.findByEmail(email);
//
//        // Ellenőrizze, hogy a felhasználó ne legyen null, mielőtt hozzáférne az attribútumokhoz.
//        if (user != null) {
//            user.setStatus(0);
//            userRepository.save(user);
//        }
//    }
    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        //modifyUserStatus(request.getUserPrincipal().getName());
        response.sendRedirect("/login?igen");
    }

//    @Override
//    public void onExpiredSessionDetected(SessionInformationExpiredEvent event)
//            throws IOException, ServletException {
//        String email = event.getSessionInformation().getPrincipal().toString();
//        modifyUserStatus(email);
//    }
//
//    private void modifyUserStatus(String email) {
//        User user = userRepository.findByEmail(email);
//        if (user != null) {
//            user.setStatus(0);
//            userRepository.save(user);
//        }
//    }

}
