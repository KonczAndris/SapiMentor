package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.RatingRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.MyGroupService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.RatingService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.SkillService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.TopicService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility.UtilityForSomeCotroller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/myGroup")
@Controller
public class MyGroupController {

    private final UserRepository userRepository;
    private final RatingService ratingService;
    private final TopicService topicService;
    private final SkillService skillService;
    private final RatingRepository ratingRepository;
    private final UtilityForSomeCotroller utilityForSomeCotroller;
    private  final MyGroupService myGroupService;



    @Autowired
    public MyGroupController(UserRepository userRepository,
                             RatingService ratingService,
                             TopicService topicService,
                             SkillService skillService,
                             RatingRepository ratingRepository,
                             UtilityForSomeCotroller utilityForSomeCotroller,
                             MyGroupService myGroupService) {
        this.userRepository = userRepository;
        this.ratingService = ratingService;
        this.topicService = topicService;
        this.skillService = skillService;
        this.ratingRepository = ratingRepository;
        this.utilityForSomeCotroller = utilityForSomeCotroller;
        this.myGroupService = myGroupService;
    }

    @GetMapping("")
    public String showMyGroup(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        utilityForSomeCotroller.showTopicsToMyGroupPage(model);
        utilityForSomeCotroller.showSkillsToMyGroupPage(model);
        utilityForSomeCotroller.showProfileImageAndName(model, principal);

        return "myGroup";
    }

    @Cacheable("showAllMentees")
    @GetMapping("/mentees")
    public String showAllMentees (Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        utilityForSomeCotroller.showTopicsToMyGroupPage(model);
        utilityForSomeCotroller.showSkillsToMyGroupPage(model);
        utilityForSomeCotroller.showProfileImageAndName(model, principal);

        // ez helyett esetleg a hosszu menet
        myGroupService.getAllMeneesDetails(model, principal);

        return "myGroup";
    }


    @Cacheable("showAllMentors")
    @GetMapping("/mentors")
    public String showAllMentors (Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        utilityForSomeCotroller.showTopicsToMyGroupPage(model);
        utilityForSomeCotroller.showSkillsToMyGroupPage(model);
        utilityForSomeCotroller.showProfileImageAndName(model, principal);

        // ez helyett esetleg a hosszu menet
        myGroupService.getAllMentorsDetails(model, principal);

        return "myGroup";
    }

    @Cacheable("getallmenteesprofileimage")
    @GetMapping("/mentees/getallmenteeprofileimage")
    public ResponseEntity<Map<String,Object>> getAllMenteeProfileImage(Principal principal) {
        try {
            String email = principal.getName();
            Long userId = userRepository.findIdByEmail(email);
            Map<String, Object> response = new HashMap<>();
            List<Object[]> menteeprofileImageBytesList = myGroupService.getAllMenteeProfileImageById(userId);
            response.put("profileimagesandid", menteeprofileImageBytesList);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Cacheable("getallmentorsprofileimage")
    @GetMapping("/mentors/getallmentorprofileimage")
    public ResponseEntity<Map<String,Object>> getAllMentorProfileImage(Principal principal) {
        try {
            String email = principal.getName();
            Long userId = userRepository.findIdByEmail(email);
            Map<String, Object> response = new HashMap<>();
            List<Object[]> mentorprofileImageBytesList = myGroupService.getAllMentorProfileImageById(userId);
            response.put("profileimagesandid", mentorprofileImageBytesList);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during saving rating!");
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during revoking!");
        }
    }

    @PostMapping("/saveFavorite")
    public ResponseEntity<String> saveFavorite(@RequestParam Long favoriteUserId,
                                             Principal principal) {
        try {
            myGroupService.saveFavorite(principal, favoriteUserId);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during saving favourite!");
        }

    }

    @PostMapping("/revokeFavorite")
    public ResponseEntity<String> revokeFavorite(@RequestParam Long favoriteUserId,
                                             Principal principal) {
        try {
            myGroupService.revokeFavorite(principal, favoriteUserId);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during revoking favourite!");
        }
    }

    @GetMapping("/getSelectedUserDetails")
    public ResponseEntity<String> showSelectedUserDetails(@RequestParam Long selectedUserId,
                                                          Model model) {
        try {
            myGroupService.getSelectedUserProfile(selectedUserId, model);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during revoking favourite!");
        }
    }

//    @Cacheable("showAllMentors")
//    @GetMapping("/mentors")
//    public String showAllMentors (Model model, Principal principal) {
//        if (principal == null) {
//            return "redirect:/login";
//        }
//        utilityForSomeCotroller.showTopicsToMyGroupPage(model);
//        utilityForSomeCotroller.showSkillsToMyGroupPage(model);
//        utilityForSomeCotroller.showProfileImageAndName(model, principal);
//
//        // ez helyett esetleg a hosszu menet
//        myGroupService.getAllMentorsDetails(model, principal);
//
//        return "myGroup";
//    }

    @GetMapping("/mentees/filtered")
    public ResponseEntity<String> showFilteredMentees(Model model, Principal principal, @RequestParam MultiValueMap<String, String> params) {
        try {
            System.out.println("searchData: " + params);

//            params.forEach((key, values) -> {
//                System.out.println(key + ": " + values);
//            });

            // JSON reprezentáció a params-ról
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonRepresentation = objectMapper.writeValueAsString(params);

            System.out.println("JSON reprezentáció: " + jsonRepresentation);

            myGroupService.getAllMenteesDetailsByFilter(principal, params);

            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
