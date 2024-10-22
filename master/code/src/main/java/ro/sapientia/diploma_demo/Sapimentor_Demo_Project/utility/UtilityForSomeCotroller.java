package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config.UserRegistrationDetails;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Role;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Skill;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Topic;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.SkillService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.TopicService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

@Service
public class UtilityForSomeCotroller {
    private final UserRepository userRepository;
    private final TopicService topicService;
    private final SkillService skillService;

    @Autowired
    public UtilityForSomeCotroller(UserRepository userRepository,
                                   TopicService topicService,
                                   SkillService skillService){
        this.userRepository = userRepository;
        this.topicService = topicService;
        this.skillService = skillService;
    }

    public void showProfileImageAndName(Model model, Principal principal) {
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
        model.addAttribute("mygroupUserId", user.getId());
    }

    public void showTopicsToMyGroupPage(Model model){
        List<Topic> topics = topicService.getAllTopics();
        model.addAttribute("topics", topics);
    }

    public void showSkillsToMyGroupPage(Model model){
        List<Skill> skills = skillService.getAllSkills();
        model.addAttribute("skills", skills);
    }

    public void getUserRolesToDisplayMentorSelector(Model model, Principal principal){
        String email = principal.getName();

        Long userId = userRepository.findIdByEmail(email);
        Collection<Role> roles = userRepository.findRolesByUserId(userId);

        List<String> rolesToDisplayResources = new ArrayList<>();
        boolean hasOtherRole = false;

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
        model.addAttribute("userRolesToDisplayMentorSelector", rolesAsString);
    }


}
