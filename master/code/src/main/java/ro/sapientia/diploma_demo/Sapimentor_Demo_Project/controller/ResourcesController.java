package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Resources;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Role;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Topic;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ResourcesRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.ResourceServices;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.TopicService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/resources")
@Controller
public class ResourcesController {
    private final UserRepository userRepository;
    private final TopicService topicService;
    private final ResourcesRepository resourcesRepository;

    private final ResourceServices resourceServices;
    private final SseController sseController;

    @Autowired
    public ResourcesController(UserRepository userRepository,
                               TopicService topicService,
                               ResourcesRepository resourcesRepository,
                               ResourceServices resourceServices,
                               SseController sseController) {
        this.userRepository = userRepository;
        this.topicService = topicService;
        this.resourcesRepository = resourcesRepository;
        this.resourceServices = resourceServices;
        this.sseController = sseController;
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

    @GetMapping("")
    public String showResources(Model model, Principal principal) {
        showUserRolesToDisplayResources(model, principal);
        showTopicsToDisplayResources(model, principal);
        List<Resources> resources = resourceServices.getAllResources();
        //System.out.println("Resources: " + resources);
        model.addAttribute("resourcesData", resources);

        return "resources";
    }
    @GetMapping("/examExamples")
    public String showExamExamples(Model model, Principal principal) {
        showUserRolesToDisplayResources(model, principal);
        showTopicsToDisplayResources(model, principal);
        return "examExamples";
    }
    @GetMapping("/diplomaTheses")
    public String showDiplomaTheses(Model model, Principal principal) {
        showUserRolesToDisplayResources(model, principal);
        showTopicsToDisplayResources(model, principal);
        return "diplomaTheses";
    }

    @PostMapping("/uploadResources")
    public ResponseEntity<String> uploadResources(String resourcesUploadDataItems,
                                                  Principal principal){
        //ez a objectMapper a json stringet alakitja at objektumokka
        ObjectMapper objectMapper = new ObjectMapper();
        //System.out.println("Resources upload data items: " + resourcesUploadDataItems);

        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        //System.out.println("User_Name: " + user.getFirst_Name());
        try{
            // JSON string deszerializálása objektumokká
            Resources[] resources_dataItems = objectMapper.readValue(resourcesUploadDataItems, Resources[].class);
            //System.out.println("Resources data items: " + resources_dataItems);

            for (Resources resourcesData : resources_dataItems){
                String name = resourcesData.getName();
                String link = resourcesData.getLink();
                String topic_name = resourcesData.getTopic_name();
                String user_name = user.getFirst_Name() + " " + user.getLast_Name();
                Integer like = 0;
                Integer dislike = 0;
//                System.out.println("Name: " + name);
//                System.out.println("Link: " + link);
//                System.out.println("Topic_name: " + topic_name);
//                System.out.println("User_name: " + user_name);
//                System.out.println("Like: " + like);
//                System.out.println("Dislike: " + dislike);

                // Adatok elmentese a Resources entitasba
                Resources resources = new Resources();
                resources.setName(name);
                resources.setLink(link);
                resources.setTopic_name(topic_name);
                resources.setUser_name(user_name);
                resources.setLike(like);
                resources.setDislike(dislike);

                // Resources entitas elmentese az adatbazisba
                resourcesRepository.save(resources);

                sseController.sendSseMessage(resources.getLike() + "/" + resources.getDislike());


            }

            return ResponseEntity.ok("Sikeres Feltoltes");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @PostMapping("/like")
    public ResponseEntity<String> likeResource(@RequestParam Long resourceId) {
        System.out.println("Resource ID: " + resourceId);
        resourceServices.likeResource(resourceId); // Hívjuk meg a service rétegben lévő likeResource metódust
        return ResponseEntity.ok("Liked resource with ID: " + resourceId);
    }

    @PostMapping("/dislike")
    public ResponseEntity<String> dislikeResource(@RequestParam Long resourceId) {
        resourceServices.dislikeResource(resourceId); // Hívjuk meg a service rétegben lévő dislikeResource metódust
        return ResponseEntity.ok("Disliked resource with ID: " + resourceId);
    }

}
