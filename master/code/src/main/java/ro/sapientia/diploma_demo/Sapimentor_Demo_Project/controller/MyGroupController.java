package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.RatingService;

import java.security.Principal;

@RequestMapping("/mygroup")
@Controller
public class MyGroupController {

    private final UserRepository userRepository;
    private final RatingService ratingService;

    @Autowired
    public MyGroupController(UserRepository userRepository,
                             RatingService ratingService) {
        this.userRepository = userRepository;
        this.ratingService = ratingService;
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
