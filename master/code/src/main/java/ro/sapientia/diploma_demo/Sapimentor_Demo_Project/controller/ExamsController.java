package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config.UserRegistrationDetails;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Role;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Topic;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.ResourceServices;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.TopicService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RequestMapping("/resources")
@Controller
public class ExamsController {
    private final UserRepository userRepository;
    private final TopicService topicService;
    private final ResourceServices resourceServices;

    @Autowired
    public ExamsController(UserRepository userRepository,
                           TopicService topicService,
                           ResourceServices resourceServices) {
        this.userRepository = userRepository;
        this.topicService = topicService;
        this.resourceServices = resourceServices;
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
        //System.out.println("Roles: " + rolesAsString);
        model.addAttribute("userRolesToDisplayResources", rolesAsString);
    }

    private void showTopicsToDisplayResources(Model model, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        // Itt lekérem a témákat a service segítségével
        List<Topic> topics = topicService.getAllTopics();
        //System.out.println("Topics: " + topics);
        model.addAttribute("topics", topics);
    }

    private void showProfileImageAndName(Model model, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(user);
        //System.out.println("User: " + userRegistrationDetails.getFirstName());

        byte[] profileImage = user.getProfileImage();
        if(profileImage != null){
            String profileImageBase64 = Base64.getEncoder().encodeToString(profileImage);
            model.addAttribute("profileImageBase64", profileImageBase64);
        } else {
            model.addAttribute("profileImageBase64", "");
        }

        model.addAttribute("userRegistrationDetails", userRegistrationDetails);
    }

    @GetMapping("/examExamples")
    public String showExamExamples(Model model, Principal principal) {
        showUserRolesToDisplayResources(model, principal);
        showTopicsToDisplayResources(model, principal);
        showProfileImageAndName(model, principal);
        return "examExamples";
    }

    @PostMapping("/examExamples/uploadExams")
    public ResponseEntity<String> uploadExams(@RequestParam("image") MultipartFile image,
                                              @RequestParam("name") String name,
                                              @RequestParam("topic") String topic,
                                              Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        if (user != null) {
            String user_name = user.getFirst_Name() + " " + user.getLast_Name();
            try {
                String errorMessage = resourceServices.uploadExamImage(image, name, topic, user_name);
                System.out.println("Error message: " + errorMessage);
                if(errorMessage != null){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
                }

                //resourceServices.uploadExamImage(image, name, topic, user_name);

                return ResponseEntity.ok("Success");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The user is not logged in!");
    }
}
