package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

;

@Controller
public class HandleExpiredSessionController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/handleExpiredSession")
    public String handleExpiredSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();

            // Ide helyezd el azokat a lépéseket, amelyeket a lejárt munkamenettel kapcsolatosan meg kell tenni
            if (email != null) {
                User user = userRepository.findByEmail(email);
                if (user != null) {
                    user.setStatus(0);
                    userRepository.save(user);
                } else {
                    // Logolás vagy más kezelés, ha a felhasználó null
                    System.out.println("User is null for email: " + email);
                }
            } else {
                // Logolás vagy más kezelés, ha az email null
                System.out.println("Email is null");
            }
        }
        System.out.println("authentication is null" + authentication);
        // A visszairányítás a login oldalra, vagy a megfelelő helyre
        return "redirect:/login?logout";
    }
}
