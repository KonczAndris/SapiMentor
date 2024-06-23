package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config.UserRegistrationDetails;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Role;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Topics_Comment;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.TopicService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.Topics_CommentService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility.UserProfileNotification;

import java.security.Principal;
import java.util.*;

@RequestMapping("/topics")
@Controller
public class TopicsController {
    private final UserRepository userRepository;
    private final TopicService topicService;
    private final UserProfileNotification userProfileNotification;
    private final Topics_CommentService topicsCommentService;

    @Autowired
    public TopicsController(UserRepository userRepository,
                            TopicService topicService,
                            UserProfileNotification userProfileNotification,
                            Topics_CommentService topicsCommentService) {
        this.userRepository = userRepository;
        this.topicService = topicService;
        this.userProfileNotification = userProfileNotification;
        this.topicsCommentService = topicsCommentService;
    }

    private void showUserRolesToDisplayTopics(Model model, Principal principal){

        String email = principal.getName();

        // Uj megoldas (probalkozas)
        Long userId = userRepository.findIdByEmail(email);
        Collection<Role> roles = userRepository.findRolesByUserId(userId);


        List<String> rolesToDisplayResources = new ArrayList<>();
        boolean hasOtherRole = false;

        // Uj megoldas (probalkozas)
        for (Role role : roles){
            if(!role.getName().equals("USER")){
                rolesToDisplayResources.add(role.getName());
                hasOtherRole = true;
            }
        }

        if(!hasOtherRole){
            rolesToDisplayResources.add("USER");
        }

        String rolesAsString = String.join(",", rolesToDisplayResources);
        System.out.println("Roles: " + rolesAsString);
        model.addAttribute("userRolesToDisplayTopics", rolesAsString);
    }


    private void showProfileImageAndName(Model model, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(user);

        byte[] profileImage = user.getProfileImage();
        if(profileImage != null){
            String profileImageBase64 = Base64.getEncoder().encodeToString(profileImage);
            model.addAttribute("topicsImageBase64", profileImageBase64);
        } else {
            model.addAttribute("topicsImageBase64", "");
        }

        model.addAttribute("userRegistrationDetails", userRegistrationDetails);
        model.addAttribute("topicsUserId", user.getId());
    }

    @GetMapping("")
    public String showTopics(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        showUserRolesToDisplayTopics(model, principal);
        showProfileImageAndName(model, principal);

        return "topics";
    }

    @GetMapping("/getTopicsProfileNotificationStatus")
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

    @PostMapping("/saveComment")
    public ResponseEntity<String> saveComment(@RequestBody Topics_Comment topicsComment,
                                              Principal principal) {
        if (principal == null) {
            return ResponseEntity.ok("ERROR");
        }
        String userEmail = principal.getName();

        try {
            topicsCommentService.saveTopicComment(userEmail, topicsComment);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during saving comment!");
        }
    }

    @GetMapping("/getSelectedTopicDetails")
    public ResponseEntity<List<Object>> showSelectedUserDetails(@RequestParam String selectedTopicId,
                                          Model model,
                                          Principal principal) {
        try {
            List<Object> allCommentDataForTopics = topicsCommentService.getSelectedTopicComments(selectedTopicId, model, principal);
            if(allCommentDataForTopics == null){
                return ResponseEntity.ok(Collections.singletonList("ERROR"));
            } else {
                return ResponseEntity.ok(allCommentDataForTopics);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonList("Error during getting selected topic details!"));
        }
    }

    @GetMapping("/getSelectedUsersImages")
    public ResponseEntity<Map<String,Object>> getSelectedUsersImages(@RequestParam String selectedTopicId,
                                                                     Principal principal) {
        try {
            if (principal == null) {
                return ResponseEntity.ok(Collections.singletonMap("ERROR", "ERROR"));
            }
            Map<String, Object> response = new HashMap<>();
            List<Object[]> selectedUserImages = topicsCommentService.getSelectedUserImages(selectedTopicId);

            response.put("selectedUserImages", selectedUserImages);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
