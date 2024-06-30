package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config.UserRegistrationDetails;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.*;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.*;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.RatingService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.TopicService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.UserServiceImpl;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility.UserProfileNotification;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;

@Controller
public class ProfileController {
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final TopicService topicService;
    private final SkillRepository skillRepository;
    private final TopicRepository topicRepository;
    private final ProfileTopicsRepository profileTopicsRepository;
    private final RatingService ratingService;
    private final RoleRepository roleRepository;
    private final UserProfileNotification userProfileNotification;


    @Autowired
    public ProfileController(UserRepository userRepository,
                             UserServiceImpl userService,
                             TopicService topicService,
                             SkillRepository skillRepository,
                             TopicRepository topicRepository,
                             ProfileTopicsRepository profileTopicsRepository,
                             RatingService ratingService,
                             RoleRepository roleRepository,
                             UserProfileNotification userProfileNotification) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.topicService = topicService;
        this.skillRepository = skillRepository;
        this.topicRepository = topicRepository;
        this.profileTopicsRepository = profileTopicsRepository;
        this.ratingService = ratingService;
        this.roleRepository = roleRepository;
        this.userProfileNotification = userProfileNotification;
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal,
                              @RequestParam(value = "errorMessage", required = false) String errorMessage,
                              @RequestParam(value = "selectedTopic", required = false) String selectedTopic) {
        if (principal == null) {
            return "redirect:/login";
        }
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(user);

        List<String> rolesToDisplay = new ArrayList<>();
        boolean hasOtherRoles = false;

        for (Role role : user.getRoles()) {
            if (!role.getName().equals("USER")) {
                rolesToDisplay.add(role.getName());
                hasOtherRoles = true;
            }
        }

        if (!hasOtherRoles) {
            rolesToDisplay.add("USER");
        }

        String rolesAsString = String.join(", ", rolesToDisplay);
        model.addAttribute("userRolesToDisplay", rolesAsString);

        List<Topic> topics = topicService.getAllTopics();

        List<Profile_Topics> userTopics = profileTopicsRepository.findByUserId(user.getId());

        if (!userTopics.isEmpty()) {
            model.addAttribute("userTopics", userTopics);
        }

        byte[] profileImageBytes = user.getProfileImage();

        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }

        if (profileImageBytes != null) {
            String profileImageBase64 = Base64.getEncoder().encodeToString(profileImageBytes);
            model.addAttribute("profileImageBase64", profileImageBase64);
        } else {
            model.addAttribute("profileImageBase64", "");
        }

        model.addAttribute("topics", topics);
        model.addAttribute("userRegistrationDetails", userRegistrationDetails);

        Map<String, Double> ratingData = ratingService.getAverageRating(user.getId());
        double average = ratingData.get("average");
        int count = ratingData.get("count").intValue();
        model.addAttribute("averageOfRating", average);
        model.addAttribute("countOfRating", count);
        model.addAttribute("UserId", user.getId());

        userService.getUserComments(model, principal);

        return "profile";
    }

    @GetMapping("/getSelectedUsersImagesForProfilePage")
    public ResponseEntity<Map<String,Object>> getSelectedUserImagesForProfilePage(Principal principal){
        try {
            Map<String, Object> response = new HashMap<>();
            List<Object[]> selectedUserImagesForProfilePage = userService.getSelectedUserImagesForProfilePage(principal);

            response.put("selectedUserImagesForProfilePage", selectedUserImagesForProfilePage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @PostMapping("/profile/save")
    public String saveProfileData(@RequestParam("firstName") String firstName,
                                  @RequestParam("lastName") String lastName,
                                  @RequestParam("specialization") String specialization,
                                  @RequestParam("year") Integer year,
                                  @RequestParam("phoneNumber") String phoneNumber,
                                  Principal principal) {

        String email = principal.getName();
        userService.saveProfileData(email,firstName, lastName, specialization, year, phoneNumber);

        return "redirect:/profile";
    }

    @PostMapping("/profile/upload-profile-image")
    public String uploadProfileImage(final @RequestParam("image") MultipartFile image,
                                     Principal principal,
                                     Model model) {
        String username = principal.getName();

        String errorMessage = userService.uploadProfileImage(username, image);
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            return "profile?errorMessage=" + UriUtils.encode(errorMessage, StandardCharsets.UTF_8);
        }

        userService.uploadProfileImage(username, image);
        User user = userRepository.findByEmail(username);
        UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(user);
        model.addAttribute("userRegistrationDetails", userRegistrationDetails);

        return "profile";
    }


    @GetMapping("/getSkills")
    @ResponseBody
    public List<Skill> getSkillsByTopic(@RequestParam String selectedTopic) {
        return topicService.getSkillsByTopic(selectedTopic);
    }

    @GetMapping("/getUserTopicsAndSkills")
    @ResponseBody
    public List<Profile_Topics> getUserTopicsAndSkills(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        List<Profile_Topics> userTopicsAndSkills = profileTopicsRepository.findByUserId(user.getId());

        return userTopicsAndSkills;
    }

    @PostMapping("/saveProfileTopics")
    public ResponseEntity<String> saveProfileTopics(String profileTopicsDataItems,
                                    Principal principal) {
        //ez a objectMapper a json stringet alakitja at objektumokka
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // JSON string deszerializálása objektumokká
            Profile_Topics_DataItem[] dataItems = objectMapper.readValue(profileTopicsDataItems, Profile_Topics_DataItem[].class);

            for (Profile_Topics_DataItem dataItem : dataItems) {
                String id = dataItem.getId();
                String topic = dataItem.getTopic();
                List<String> skills = dataItem.getSkills();

                if(!id.equals("")){
                    Profile_Topics existingProfileTopic = profileTopicsRepository.findById(Long.parseLong(id)).orElse(null);
                    if(existingProfileTopic != null){
                        existingProfileTopic.setTopic(topic);
                        existingProfileTopic.setTags(skills);
                        profileTopicsRepository.save(existingProfileTopic);
                    }
                }else{
                    Profile_Topics profileTopics = new Profile_Topics();
                    profileTopics.setTopic(topic);
                    profileTopics.setTags(skills);
                    String email = principal.getName();
                    User user = userRepository.findByEmail(email);
                    profileTopics.setUser(user);
                    profileTopicsRepository.save(profileTopics);
                }
            }
            return ResponseEntity.ok("Success");
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/deleteTopicAndSkills")
    public ResponseEntity<String> deleteTopicAndSkills(@RequestParam("topicId") Long topicId) {
        try {
            Profile_Topics topicToDelete = profileTopicsRepository.findById(topicId).orElse(null);
            if(topicToDelete != null){
                profileTopicsRepository.delete(topicToDelete);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The topic does not exist!");
            }

            return ResponseEntity.ok("Sikeres törlés");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during deleting topic!");
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/updateUserRoleStatus")
    public ResponseEntity<String> updateUserRoleStatus(String selectedRole,
                                                       Principal principal) throws JsonProcessingException {
        String email = principal.getName();
        Long userId = userRepository.findByEmail(email).getId();

        if(!userService.isAtLeastSecondYear(userId)){
            Map<String, String> responsee = new HashMap<>();
            responsee.put("message", "NEM_MASOD");
            return ResponseEntity.ok(new ObjectMapper().writeValueAsString(responsee));
        }else{
            User user = userRepository.findByEmail(email);
            if (selectedRole.length() > 6) {
                String[] rolesArray = selectedRole.split(",");
                for (String role : rolesArray) {
                    Role newRole = new Role(role.trim());
                    user.addRole(newRole);
                    System.out.println("New role added: " + newRole);
                }
            } else if (selectedRole.length() == 6 ) {
                Role newRole = new Role(selectedRole);
                user.addRole(newRole);
            }

            userRepository.save(user);
        }

        // Az eredmény JSON objektum létrehozása
         Map<String, String> response = new HashMap<>();
         response.put("message", "MODOSITVA");

        // JSON objektum visszaadása a ResponseEntity segítségével
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(response));
    }

    @Transactional
    @PostMapping("/deleteUserRoleStatus")
    public ResponseEntity<String> deleteUserRoleStatus(String selectedRoleToDelete, Principal principal)
            throws JsonProcessingException {
        System.out.println("Selected role for delete: " + selectedRoleToDelete);
        String email = principal.getName();
        Long userId = userRepository.findIdByEmail(email);

        User user = userRepository.findByEmail(email);
        List<Integer> removedRoles = new ArrayList<>();
        if (selectedRoleToDelete.length() > 6) {
            String[] rolesArray = selectedRoleToDelete.split(",");
            for (String role : rolesArray) {
                String removableRole = role.trim();
                // Törölje el a kiválasztott szerepet a User objektumból
                removedRoles.addAll(user.removeRoleFromUser(user, removableRole));
            }
        } else if (selectedRoleToDelete.length() == 6) {
            removedRoles.addAll(user.removeRoleFromUser(user, selectedRoleToDelete.trim()));
        }

        userRepository.save(user);

        for (Integer removedRole : removedRoles) {
            System.out.println("Removed role: " + removedRole);
            roleRepository.updateRolesStatusByIdCustom(removedRole);
        }

        // Az eredmény JSON objektum létrehozása
        Map<String, String> response = new HashMap<>();
        response.put("message", "TOROLVE");

        // JSON objektum visszaadása a ResponseEntity segítségével
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(response));
    }

    @GetMapping("/profile/getProfileNotificationStatus")
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



