package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config.UserRegistrationDetails;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Topic;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.RatingService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.TopicService;

import java.security.Principal;
import java.util.Base64;
import java.util.List;

@RequestMapping("/myGroup")
@Controller
public class MyGroupController {

    private final UserRepository userRepository;
    private final RatingService ratingService;
    private final TopicService topicService;



    @Autowired
    public MyGroupController(UserRepository userRepository,
                             RatingService ratingService,
                             TopicService topicService) {
        this.userRepository = userRepository;
        this.ratingService = ratingService;
        this.topicService = topicService;
    }

    //TODO: Implement showProfileImageAndName
    private void showProfileImageAndName(Model model, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(user);

        byte[] profileImage = user.getProfileImage();
        if (profileImage != null) {
            String profileImageBase64 = Base64.getEncoder().encodeToString(profileImage);
            model.addAttribute("profileImageBase64", profileImageBase64);
        } else {
            model.addAttribute("profileImageBase64", "");
        }

        model.addAttribute("userRegistrationDetails", userRegistrationDetails);
    }

    private void showTopicsToDisplayUsersTypes(Model model){
        // Itt lekérem a témákat a service segítségével
        List<Topic> topics = topicService.getAllTopics();
        //System.out.println("Topics: " + topics);
        model.addAttribute("topics", topics);
    }


    @GetMapping("")
    public String showMyGroup(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        showTopicsToDisplayUsersTypes(model);
        showProfileImageAndName(model, principal);
        return "myGroup";
    }

    @PostMapping("/saveRating")
    public ResponseEntity<String> saveRating(@RequestParam String ratedUserEmail,
                                             @RequestParam int score,
                                             Principal principal) {
        String ratingUserEmail = principal.getName();
        try {
            ratingService.saveRating(ratingUserEmail, ratedUserEmail, score);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hiba történt a mentés során!");
        }

    }

    @PostMapping("/revokeRating")
    public ResponseEntity<String> revokeRating(@RequestParam String ratedUserEmail,
                                             Principal principal) {
        String ratingUserEmail = principal.getName();
        try {
            ratingService.revokeRating(ratingUserEmail, ratedUserEmail);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hiba történt a mentés során!");
        }
    }

}
