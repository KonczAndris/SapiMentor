package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config.UserRegistrationDetails;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Role;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.TopicService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

@RequestMapping("/topics")
@Controller
public class TopicsController {
    private final UserRepository userRepository;
    private final TopicService topicService;

    @Autowired
    public TopicsController(UserRepository userRepository, TopicService topicService) {
        this.userRepository = userRepository;
        this.topicService = topicService;
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
}