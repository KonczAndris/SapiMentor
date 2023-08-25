package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config.UserRegistrationDetails;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.UserServiceImpl;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Base64;

@Controller
public class ProfileController {
    private final UserRepository userRepository;
    private final UserServiceImpl userService;

    @Autowired
    public ProfileController(UserRepository userRepository, UserServiceImpl userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal,
                              @RequestParam(value = "errorMessage", required = false) String errorMessage) {
        String email = principal.getName(); // Bejelentkezett felhasználó neve

        // Felhasználó lekérdezése az adatbázisból a felhasználónév alapján
        User user = userRepository.findByEmail(email);

        UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(user);
        //System.out.println(userRegistrationDetails);

        //Profil kep megjelenitese
        // A profil kép byte tömbje
        byte[] profileImageBytes = user.getProfileImage();
        // A profil kép Base64 kódolása

        //A regi verzional ez a sor nem kell !!!!
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }

        if (profileImageBytes != null) {
            String profileImageBase64 = Base64.getEncoder().encodeToString(profileImageBytes);
            model.addAttribute("profileImageBase64", profileImageBase64);
        } else {
            model.addAttribute("profileImageBase64", "");
        }
        model.addAttribute("userRegistrationDetails", userRegistrationDetails);

        return "profile";
    }

    @PostMapping("/profile/save")
    public String saveProfileData(@RequestParam("firstName") String firstName,
                                  @RequestParam("lastName") String lastName,
                                  @RequestParam("specialization") String specialization,
                                  @RequestParam("year") Integer year,
                                  @RequestParam("phoneNumber") String phoneNumber,
                                  Principal principal) {
        // Bejelentkezett felhasználó neve
        String email = principal.getName(); // Bejelentkezett felhasználó neve
        //System.out.println(phoneNumber);



                // Adatok mentése a szolgáltatás segítségével
                userService.saveProfileData(email,firstName, lastName, specialization, year, phoneNumber);

        return "redirect:/profile";// Visszatérés a profil oldalra
    }


//    @PostMapping("/upload-profile-image")
//    public String uploadProfileImage(final @RequestParam("image") MultipartFile image,
//                                     Principal principal,
//                                     Model model,
//                                     HttpServletResponse response) {
//        String username = principal.getName(); // Aktuális felhasználó neve
//
//        userService.uploadProfileImage(username, image);
//
//        // Frissítsük a felhasználó adatait a modellben
//        User user = userRepository.findByEmail(username);
//        UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(user);
//        System.out.println("IGENJASFJASHKF" + userRegistrationDetails);
//        model.addAttribute("userRegistrationDetails", userRegistrationDetails);
//
//        return "redirect:/profile"; // Visszairányítás a profil oldalra
//    }

    @PostMapping("/upload-profile-image")
    public String uploadProfileImage(final @RequestParam("image") MultipartFile image,
                                     Principal principal,
                                     Model model) {
        String username = principal.getName();

        //A regi verzional ez a sor es az if nem kell !!!!
        String errorMessage = userService.uploadProfileImage(username, image);
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            return "redirect:/profile?errorMessage=" + UriUtils.encode(errorMessage, StandardCharsets.UTF_8);
        }

        userService.uploadProfileImage(username, image);
        User user = userRepository.findByEmail(username);
        UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(user);
        model.addAttribute("userRegistrationDetails", userRegistrationDetails);

        //System.out.println("User details after upload: " + userRegistrationDetails);
        return "redirect:/profile";
    }

}
