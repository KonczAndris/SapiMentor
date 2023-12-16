package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.MyGroupProfileDetailDTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Rating;
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

    //TODO: Implement showProfileImageAndName
//    private void showProfileImageAndName(Model model, Principal principal) {
//        String email = principal.getName();
//        User user = userRepository.findByEmail(email);
//        UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(user);
//
//        byte[] profileImage = user.getProfileImage();
//        if (profileImage != null) {
//            String profileImageBase64 = Base64.getEncoder().encodeToString(profileImage);
//            model.addAttribute("profileImageBase64", profileImageBase64);
//        } else {
//            model.addAttribute("profileImageBase64", "");
//        }
//
//        model.addAttribute("userRegistrationDetails", userRegistrationDetails);
//    }

//    private void showTopicsToMyGroupPage(Model model){
//        // Itt lekérem a témákat a service segítségével
//        List<Topic> topics = topicService.getAllTopics();
//        //System.out.println("Topics: " + topics);
//        model.addAttribute("topics", topics);
//    }

//    private void showSkillsToMyGroupPage(Model model){
//        List<Skill> skills = skillService.getAllSkills();
//        //System.out.println("Skills: " + skills.toString());
//        model.addAttribute("skills", skills);
//    }



//    @GetMapping("")
//    public String showMyGroup(Model model, Principal principal) {
//        if (principal == null) {
//            return "redirect:/login";
//        }
//        showTopicsToMyGroupPage(model);
//        showSkillsToMyGroupPage(model);
//        showProfileImageAndName(model, principal);
//
//        // Lekérjük az összes felhasználót a UserRepository segítségével
//        List<User> allUsers = userRepository.findAll();
//
//        // Az összes felhasználót hozzáadjuk a modellhez
//        model.addAttribute("allUsers", allUsers);
//
//        System.out.println("AllUsers: " + allUsers);
//        // Lekérjük az értékeléseket a RatingRepository segítségével
//        List<Rating> allRatings = ratingRepository.findAll();
//
//        System.out.println("AllRatings: " + allRatings);
//
////// Az értékeléseket a felhasználókhoz rendeljük a modellben
////        Map<String, List<Rating>> ratingsByUserId = allRatings.stream()
////                .collect(Collectors.groupingBy(rating -> String.valueOf(rating.getRatedUserId())));
////
////        // Az értékeléseket hozzáadjuk a modellhez a felhasználókhoz
////        model.addAttribute("ratingsByUserId", ratingsByUserId);
////        System.out.println("ratingsByUserId: " + ratingsByUserId);
//        // Az értékeléseket és az átlagokat a felhasználókhoz rendeljük a modellben
//        // Az értékeléseket és az átlagokat a felhasználókhoz rendeljük a modellben
//        Map<String, Double> averageRatingsByUserId = new HashMap<>();
//        for (User user : allUsers) {
//            double averageRating = ratingService.getAverageRating(user.getId()).get("average");
//            averageRatingsByUserId.put(String.valueOf(user.getId()), averageRating);
//        }
//
//        // Az átlagokat hozzáadjuk a modellhez a felhasználókhoz
//        model.addAttribute("averageRatingsByUserId", averageRatingsByUserId);
//        System.out.println("averageRatingsByUserId: " + averageRatingsByUserId);
//
//
//        return "myGroup";
//    }

    @GetMapping("")
    public String showMyGroup(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        utilityForSomeCotroller.showTopicsToMyGroupPage(model);
        utilityForSomeCotroller.showSkillsToMyGroupPage(model);
        utilityForSomeCotroller.showProfileImageAndName(model, principal);

        // ez itt csak proba ezt szepen meg kene csinalni fuggvenybe

        // regi verzio
        //List<User> allUsers = userRepository.findAll();

        // itt majd ugy is ugy kell lekerdezeseket
        // csinalni hogy a felhasznalo sajat magat ne lassa es
        // hogy mentor vagy mentoraltakat keressen


        // ezeknek majd kell kulon controller es service es repository
        // es fetch - el kell lekerni
        // uj verzio
//        String email = principal.getName();
//        Long userId = userRepository.findIdByEmail(email);
//        List<MyGroupProfileDetailDTO> allUsers = userRepository.findAllOtherUser(userId);
//        //System.out.println("allUsers: " + allUsers);
//        List<Rating> allRatings = ratingRepository.findAll();
//
//        // Map<Long, String> profileImagesByUserId = new HashMap<>();
//        Map<Long, Double> averageRatingsByUserId = new HashMap<>();
//        for (MyGroupProfileDetailDTO user : allUsers) {
//            Long user_Id = user.getId();
//            double averageRating = ratingService.getAverageRating(user_Id).get("average");
//            averageRatingsByUserId.put(user_Id, averageRating);
//        }
//
//
////        List<Rating> allRatings = ratingRepository.findAll();
////
////        Map<Long, Double> averageRatingsByUserId = new HashMap<>();
////        for (User user : allUsers) {
////            double averageRating = ratingService.getAverageRating(user.getId()).get("average");
////            averageRatingsByUserId.put(user.getId(), averageRating);
////        }
//
//        model.addAttribute("allUsers", allUsers);
//        // model.addAttribute("profileImagesByUserId", profileImagesByUserId);
//        model.addAttribute("allRatings", allRatings);
//        model.addAttribute("averageRatingsByUserId", averageRatingsByUserId);

        return "myGroup";
    }

    @GetMapping("/getallmentees")
    public String showAllMentees (Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        utilityForSomeCotroller.showTopicsToMyGroupPage(model);
        utilityForSomeCotroller.showSkillsToMyGroupPage(model);
        utilityForSomeCotroller.showProfileImageAndName(model, principal);

        // ez itt csak proba ezt szepen meg kene csinalni fuggvenybe

        // regi verzio
        //List<User> allUsers = userRepository.findAll();

        // itt majd ugy is ugy kell lekerdezeseket
        // csinalni hogy a felhasznalo sajat magat ne lassa es
        // hogy mentor vagy mentoraltakat keressen


        // ezeknek majd kell kulon controller es service es repository
        // es fetch - el kell lekerni
        // uj verzio
        String email = principal.getName();
        Long userId = userRepository.findIdByEmail(email);
        List<MyGroupProfileDetailDTO> allMentees = userRepository.findAllMentees(userId);
        List<Rating> allRatings = ratingRepository.findAllOther(userId);

        // Map<Long, String> profileImagesByUserId = new HashMap<>();
        Map<Long, Double> averageRatingsByUserId = new HashMap<>();
        for (MyGroupProfileDetailDTO user : allMentees) {
            Long user_Id = user.getId();
            double averageRating = ratingService.getAverageRating(user_Id).get("average");
            averageRatingsByUserId.put(user_Id, averageRating);
        }


//        List<Rating> allRatings = ratingRepository.findAll();
//
//        Map<Long, Double> averageRatingsByUserId = new HashMap<>();
//        for (User user : allUsers) {
//            double averageRating = ratingService.getAverageRating(user.getId()).get("average");
//            averageRatingsByUserId.put(user.getId(), averageRating);
//        }

        model.addAttribute("allUsers", allMentees);
        // model.addAttribute("profileImagesByUserId", profileImagesByUserId);
        model.addAttribute("allRatings", allRatings);
        model.addAttribute("averageRatingsByUserId", averageRatingsByUserId);

        return "myGroup";
    }


    @Cacheable("getallprofileimage")
    @GetMapping("/getallprofileimage")
    public ResponseEntity<Map<String,Object>> getAllProfileImage(Principal principal) {
        try {
            String email = principal.getName();
            Long userId = userRepository.findIdByEmail(email);
            Map<String, Object> response = new HashMap<>();
            List<Object[]> profileImageBytesList = myGroupService.getAllProfileImageById(userId);
            response.put("profileimagesandid", profileImageBytesList);
            System.out.println("profileImageBytesList: " + profileImageBytesList);

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
