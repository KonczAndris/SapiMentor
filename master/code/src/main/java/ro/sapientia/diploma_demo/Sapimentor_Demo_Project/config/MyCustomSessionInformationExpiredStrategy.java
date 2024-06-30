package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config;

import org.springframework.security.web.session.InvalidSessionStrategy;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyCustomSessionInformationExpiredStrategy implements InvalidSessionStrategy {


    private UserRepository userRepository;

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        //modifyUserStatus(request.getUserPrincipal().getName());
        response.sendRedirect("/login?igen");
    }

}
