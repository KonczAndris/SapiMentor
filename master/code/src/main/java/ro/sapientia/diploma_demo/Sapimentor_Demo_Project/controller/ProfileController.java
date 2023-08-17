package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config.UserRegistrationDetails;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import java.security.Principal;

@Controller
public class ProfileController {
    private final UserRepository userRepository;

    @Autowired
    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        String email = principal.getName(); // Bejelentkezett felhasználó neve

        // Felhasználó lekérdezése az adatbázisból a felhasználónév alapján
        User user = userRepository.findByEmail(email);

        UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(user);

        model.addAttribute("userRegistrationDetails", userRegistrationDetails);

        return "profile";
    }
}
