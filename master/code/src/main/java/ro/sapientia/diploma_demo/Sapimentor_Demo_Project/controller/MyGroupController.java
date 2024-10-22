package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Rating;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Role;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.RatingRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.MyGroupService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.RatingService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.SkillService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.TopicService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility.UserProfileNotification;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility.UtilityForSomeCotroller;

import java.security.Principal;
import java.util.*;

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
    private final UserProfileNotification userProfileNotification;



    @Autowired
    public MyGroupController(UserRepository userRepository,
                             RatingService ratingService,
                             TopicService topicService,
                             SkillService skillService,
                             RatingRepository ratingRepository,
                             UtilityForSomeCotroller utilityForSomeCotroller,
                             MyGroupService myGroupService,
                             UserProfileNotification userProfileNotification) {
        this.userRepository = userRepository;
        this.ratingService = ratingService;
        this.topicService = topicService;
        this.skillService = skillService;
        this.ratingRepository = ratingRepository;
        this.utilityForSomeCotroller = utilityForSomeCotroller;
        this.myGroupService = myGroupService;
        this.userProfileNotification = userProfileNotification;
    }

    private void showUserRolesToDisplayMyGroup(Model model, Principal principal){
        String email = principal.getName();

        Long userId = userRepository.findIdByEmail(email);
        Collection<Role> roles = userRepository.findRolesByUserId(userId);

        List<String> rolesToDisplayMyGroup = new ArrayList<>();
        boolean hasOtherRole = false;

        for (Role role : roles){
            if(!role.getName().equals("USER")){
                rolesToDisplayMyGroup.add(role.getName());
                hasOtherRole = true;
            }
        }

        if(!hasOtherRole){
            rolesToDisplayMyGroup.add("USER");
        }

        String rolesAsString = String.join(",", rolesToDisplayMyGroup);
        model.addAttribute("userRolesToDisplayMyGroup", rolesAsString);
    }

    @GetMapping("")
    public String showMyGroup(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        utilityForSomeCotroller.showTopicsToMyGroupPage(model);
        utilityForSomeCotroller.showSkillsToMyGroupPage(model);
        utilityForSomeCotroller.showProfileImageAndName(model, principal);
        showUserRolesToDisplayMyGroup(model, principal);

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
        showUserRolesToDisplayMyGroup(model, principal);

        myGroupService.getAllMeneesDetails(model, principal);
        utilityForSomeCotroller.getUserRolesToDisplayMentorSelector(model, principal);

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
        showUserRolesToDisplayMyGroup(model, principal);

        myGroupService.getAllMentorsDetails(model, principal);
        utilityForSomeCotroller.getUserRolesToDisplayMentorSelector(model, principal);

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
    public ResponseEntity<String> saveRating(@RequestBody Rating ratingRequest,
                                             Principal principal) {
        String ratingUserEmail = principal.getName();

        try {
            ratingService.saveRating(ratingUserEmail, ratingRequest);
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
    public String showSelectedUserDetails(@RequestParam Long selectedUserId,
                                                          Model model,
                                                          Principal principal) {
        try {
            myGroupService.getSelectedUserProfile(selectedUserId, model);
            myGroupService.getSelectedUserComments(selectedUserId, model, principal);
            utilityForSomeCotroller.getUserRolesToDisplayMentorSelector(model, principal);

            return "fragments/modal :: modal-content";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }



    @GetMapping("/getSelectedUsersImages")
    public ResponseEntity<Map<String,Object>> getSelectedUsersImages(@RequestParam Long selectedUserId,
                                                          Principal principal) {
        try {
            Map<String, Object> response = new HashMap<>();
            List<Object[]> selectedUserImages = myGroupService.getSelectedUserImages(selectedUserId);


            response.put("selectedUserImages", selectedUserImages);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/mentees/filtered")
    public String showFilteredMentees(Model model,
                                      Principal principal,
                                      @RequestParam MultiValueMap<String, String> params) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }

            utilityForSomeCotroller.showTopicsToMyGroupPage(model);
            utilityForSomeCotroller.showSkillsToMyGroupPage(model);
            utilityForSomeCotroller.showProfileImageAndName(model, principal);
            myGroupService.getAllMenteesDetailsByFilter(principal, model, params);
            utilityForSomeCotroller.getUserRolesToDisplayMentorSelector(model, principal);
            showUserRolesToDisplayMyGroup(model, principal);

            return "myGroup";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }


    @GetMapping("/mentors/filtered")
    public String showFilteredMentors(Model model, Principal principal, @RequestParam MultiValueMap<String, String> params) {
        try {
            if (principal == null) {
                return "redirect:/login";
            }

            utilityForSomeCotroller.showTopicsToMyGroupPage(model);
            utilityForSomeCotroller.showSkillsToMyGroupPage(model);
            utilityForSomeCotroller.showProfileImageAndName(model, principal);
            myGroupService.getAllMentorsDetailsByFilter(principal, model, params);
            utilityForSomeCotroller.getUserRolesToDisplayMentorSelector(model, principal);
            showUserRolesToDisplayMyGroup(model, principal);

            return "myGroup";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/getMyGroupProfileNotificationStatus")
    public ResponseEntity<String> getProfileNotificationStatus(Long userId) {
        if(userId != null) {
            String profileNotificationStatus = userProfileNotification.getProfileNotificationStatus(userId);
            if (profileNotificationStatus != null && profileNotificationStatus.equals("HAVE")) {
                return ResponseEntity.ok("OK");
            } else {
                return ResponseEntity.ok("NOTOK");
            }
        } else {
            return ResponseEntity.ok("ERROR");
        }
    }

}
