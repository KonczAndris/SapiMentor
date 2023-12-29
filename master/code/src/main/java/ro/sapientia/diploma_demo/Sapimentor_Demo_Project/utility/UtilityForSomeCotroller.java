package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config.UserRegistrationDetails;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Skill;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Topic;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.SkillService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.TopicService;

import java.security.Principal;
import java.util.Base64;
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
    }

    public void showTopicsToMyGroupPage(Model model){
        // Itt lekérem a témákat a service segítségével
        List<Topic> topics = topicService.getAllTopics();
        //System.out.println("Topics: " + topics);
//        for (Topic topic : topics) {
//            System.out.println("Topic: " + topic.getTopic() + " : " );
//            for (Skill skill : topic.getSkills()) {
//                System.out.println("Skill: " + skill.getSkill());
//            }
//        }
        model.addAttribute("topics", topics);
    }

    public void showSkillsToMyGroupPage(Model model){
        List<Skill> skills = skillService.getAllSkills();
        //System.out.println("Skills: " + skills.toString());
        for (Skill skill : skills) {
            System.out.println("Skill: " + skill.getSkill() + " : " + skill.getTopic().getTopic());
        }
        model.addAttribute("skills", skills);
    }

}
