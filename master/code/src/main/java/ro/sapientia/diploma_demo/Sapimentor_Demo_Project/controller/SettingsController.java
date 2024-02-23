package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config.UserRegistrationDetails;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Role;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.SendContactEmail;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.SettingsService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.TopicService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility.UserProfileNotification;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

@RequestMapping("/settings")
@Controller
public class SettingsController {
    private final UserRepository userRepository;
    private final TopicService topicService;
    private final UserProfileNotification userProfileNotification;
    private final SendContactEmail sendContactEmail;
    private final SettingsService settingsService;

    @Autowired
    public SettingsController(UserRepository userRepository,
                              TopicService topicService,
                              UserProfileNotification userProfileNotification,
                              SendContactEmail sendContactEmail,
                              SettingsService settingsService) {
        this.userRepository = userRepository;
        this.topicService = topicService;
        this.userProfileNotification = userProfileNotification;
        this.sendContactEmail = sendContactEmail;
        this.settingsService = settingsService;
    }

    private void showUserRolesToDisplaySettings(Model model, Principal principal){

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
        model.addAttribute("userRolesToDisplaySettings", rolesAsString);
    }


    private void showProfileImageAndName(Model model, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(user);

        byte[] profileImage = user.getProfileImage();
        if(profileImage != null){
            String profileImageBase64 = Base64.getEncoder().encodeToString(profileImage);
            model.addAttribute("settingsImageBase64", profileImageBase64);
        } else {
            model.addAttribute("settingsImageBase64", "");
        }

        model.addAttribute("userRegistrationDetails", userRegistrationDetails);
        model.addAttribute("settingsUserId", user.getId());
    }

    @GetMapping("")
    public String showSettings(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        showUserRolesToDisplaySettings(model, principal);

        showProfileImageAndName(model, principal);

        return "settings";
    }

    @GetMapping("/getSettingsProfileNotificationStatus")
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

    @PostMapping("/sendContactEmail")
    public String sendContactEmail( String name,
                                    String email,
                                    String message) {
        System.out.println("Email: " + name);
        System.out.println("Name: " + email);
        System.out.println("Message: " + message);

        sendContactEmail.sendContactEmail(name, email, message);

        return "redirect:/settings";
    }

    @PostMapping("/deactivateAccount")
    public ResponseEntity<String> deactivateAccount(Principal principal) {
        if(settingsService.deactivateUserAccount(principal).equals("DELETED")){
            System.out.println("Deleted: successfully");
            return ResponseEntity.ok("DELETED");
        } else {
            return ResponseEntity.ok("NOTDELETED");
        }

    }

}
