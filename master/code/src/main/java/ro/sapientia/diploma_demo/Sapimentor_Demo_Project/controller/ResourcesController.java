package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Role;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Topic;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.TopicService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ResourcesController {
    private final UserRepository userRepository;
    private final TopicService topicService;

    @Autowired
    public ResourcesController(UserRepository userRepository,
                               TopicService topicService) {
        this.userRepository = userRepository;
        this.topicService = topicService;
    }

    private void showUserRolesToDisplayResources(Model model, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        //System.out.println("User: " + user.getEmail());

        List<String> rolesToDisplayResources = new ArrayList<>();
        boolean hasOtherRole = false;

        for (Role role : user.getRoles()){
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
        model.addAttribute("userRolesToDisplayResources", rolesAsString);
    }

    private void showTopicsToDisplayResources(Model model, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        // Itt lekérem a témákat a service segítségével
        List<Topic> topics = topicService.getAllTopics();
        System.out.println("Topics: " + topics);
        model.addAttribute("topics", topics);
    }

    @GetMapping("/resources")
    public String showResources(Model model, Principal principal) {
        showUserRolesToDisplayResources(model, principal);
        showTopicsToDisplayResources(model, principal);
        return "resources";
    }
    @GetMapping("/resources/examExamples")
    public String showExamExamples(Model model, Principal principal) {
        showUserRolesToDisplayResources(model, principal);
        return "examExamples";
    }
    @GetMapping("/resources/diplomaTheses")
    public String showDiplomaTheses(Model model, Principal principal) {
        showUserRolesToDisplayResources(model, principal);
        return "diplomaTheses";
    }
}
