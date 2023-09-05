package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config.UserRegistrationDetails;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Skill;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Topic;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.SkillRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.TopicRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.TopicService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.UserServiceImpl;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Base64;
import java.util.List;

@Controller
public class ProfileController {
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final TopicService topicService;

    private final SkillRepository skillRepository;
    private final TopicRepository topicRepository;

    @Autowired
    public ProfileController(UserRepository userRepository,
                             UserServiceImpl userService,
                             TopicService topicService,
                             SkillRepository skillRepository,
                             TopicRepository topicRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.topicService = topicService;
        this.skillRepository = skillRepository;
        this.topicRepository = topicRepository;
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal,
                              @RequestParam(value = "errorMessage", required = false) String errorMessage,
                              @RequestParam(value = "selectedTopic", required = false) String selectedTopic) {
        String email = principal.getName(); // Bejelentkezett felhasználó neve

        // Felhasználó lekérdezése az adatbázisból a felhasználónév alapján
        User user = userRepository.findByEmail(email);

        UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(user);
        //System.out.println("UJ ERTEK" + userRegistrationDetails);

        // Itt lekérem a témákat a service segítségével
        List<Topic> topics = topicService.getAllTopics();

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

        // A témákat hozzáadod a modellhez
        model.addAttribute("topics", topics);
        model.addAttribute("userRegistrationDetails", userRegistrationDetails);
        System.out.println("User details: " + userRegistrationDetails);

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


    @GetMapping("/getSkills")
    @ResponseBody
    public List<Skill> getSkillsByTopic(@RequestParam String selectedTopic) {
        //System.out.println("Selected topic: " + selectedTopic);
        return topicService.getSkillsByTopic(selectedTopic);
    }



}
