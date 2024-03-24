package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config.UserRegistrationDetails;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Role;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility.UserProfileNotification;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
public class IndexController {
    private final UserRepository userRepository;
    private final UserProfileNotification userProfileNotification;

    @Autowired
    public IndexController(UserRepository userRepository,
                           UserProfileNotification userProfileNotification) {
        this.userRepository = userRepository;
        this.userProfileNotification = userProfileNotification;
    }


    @GetMapping("/")
    public String showIndex(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(user);

        List<String> rolesToDisplayIndex = new ArrayList<>();
        boolean hasOtherRole = false;

        for(Role role : user.getRoles()) {
            if(!role.getName().equals("USER")) {
                rolesToDisplayIndex.add(role.getName());
                hasOtherRole = true;
            }
        }

        if(!hasOtherRole) {
            rolesToDisplayIndex.add("USER");
        }

        String rolesAsString = String.join(",", rolesToDisplayIndex);
        model.addAttribute("userRolesToDisplayIndex", rolesAsString);

        byte[] indexprofileBytes = user.getProfileImage();

        if(indexprofileBytes != null){
            String indexImagesBase64 = Base64.getEncoder().encodeToString(indexprofileBytes);
            model.addAttribute("indexImagesBase64", indexImagesBase64);
        }else{
            model.addAttribute("indexImagesBase64", "");
        }

        model.addAttribute("userRegistrationDetails", userRegistrationDetails);
        model.addAttribute("userId", user.getId());

        return "index";
    }

    @GetMapping("/getIndexProfileNotificationStatus")
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
