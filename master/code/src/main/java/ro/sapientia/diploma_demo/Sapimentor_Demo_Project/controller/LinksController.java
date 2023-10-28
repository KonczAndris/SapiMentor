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
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config.UserRegistrationDetails;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.*;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ResourcesRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.ResourceServices;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.TopicService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.UserResourceLikeDislikeService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.VirusTotalService;

import java.security.Principal;
import java.util.*;

@RequestMapping("/resources")
@Controller
public class LinksController {
    private final UserRepository userRepository;
    private final TopicService topicService;
    private final ResourcesRepository resourcesRepository;

    private final ResourceServices resourceServices;
    private final SseController sseController;
    private final VirusTotalService virusTotalService;
    private final UserResourceLikeDislikeService userResourceLikeDislikeService;

    @Autowired
    public LinksController(UserRepository userRepository,
                           TopicService topicService,
                           ResourcesRepository resourcesRepository,
                           ResourceServices resourceServices,
                           SseController sseController,
                           VirusTotalService virusTotalService,
                           UserResourceLikeDislikeService userResourceLikeDislikeService) {
        this.userRepository = userRepository;
        this.topicService = topicService;
        this.resourcesRepository = resourcesRepository;
        this.resourceServices = resourceServices;
        this.sseController = sseController;
        this.virusTotalService = virusTotalService;
        this.userResourceLikeDislikeService = userResourceLikeDislikeService;
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

    @GetMapping("")
    public String showResources(Model model, Principal principal) {
        showUserRolesToDisplayResources(model, principal);
        showTopicsToDisplayResources(model, principal);
        List<Resources> resources = resourceServices.getAllResources();
        //System.out.println("Resources: " + resources);
        model.addAttribute("resourcesData", resources);
        showProfileImageAndName(model, principal);

        return "resources";
    }



//    @GetMapping("/examExamples")
//    public String showExamExamples(Model model, Principal principal) {
//        showUserRolesToDisplayResources(model, principal);
//        showTopicsToDisplayResources(model, principal);
//        showProfileImageAndName(model, principal);
//        return "examExamples";
//    }




//    @GetMapping("/diplomaTheses")
//    public String showDiplomaTheses(Model model, Principal principal) {
//        showUserRolesToDisplayResources(model, principal);
//        showTopicsToDisplayResources(model, principal);
//        showProfileImageAndName(model, principal);
//        return "diplomaTheses";
//    }

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
//                System.out.println("IsLinkAccessible: " + isLinkAccessible(link));
//                System.out.println("ContainsMaliciousContent: " + containsMaliciousContent(link));
                //System.out.println("IsURLSafe: " + isURLSafe(link));

                // link ervenyessegenek ellenorzese
                if (resourceServices.isLinkAccessible(link)){
                    // linkben talalhato-e karterek
                    if(!resourceServices.containsMaliciousContent(link)){
                        if(resourceServices.isURLSafe(link)){
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
                        } else {
                            // link biztonsagos-e vagy karos
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NotSafe");
                        }
                    } else {
                        // linkben talalhato karterek
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("MaliciousContent");
                    }
                } else {
                    // link ervenytelen
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("InvalidLink");
                }
            }
            return ResponseEntity.ok("Success");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }


    @PostMapping("/like")
    public ResponseEntity<String> likeResource(@RequestParam Long resourceId) {
        //System.out.println("Resource ID: " + resourceId);
        resourceServices.likeResource(resourceId);
        return ResponseEntity.ok("Liked resource with ID: " + resourceId);
    }

    @PostMapping("/dislike")
    public ResponseEntity<String> dislikeResource(@RequestParam Long resourceId) {
        resourceServices.dislikeResource(resourceId);
        return ResponseEntity.ok("Disliked resource with ID: " + resourceId);
    }

    @PostMapping("/revokelike")
    public ResponseEntity<String> RevokeLike(@RequestParam Long resourceId) {
        resourceServices.revokeLike(resourceId);
        return ResponseEntity.ok("Revoke Like resource with ID: " + resourceId);
    }

    @PostMapping("/revokedislike")
    public ResponseEntity<String> RevokeDislike(@RequestParam Long resourceId) {
        resourceServices.revokeDislike(resourceId);
        return ResponseEntity.ok("Revoke Dislike resource with ID: " + resourceId);
    }

    @PostMapping("/likeResourceAndRevokeDislike")
    public ResponseEntity<String> likeResourceAndRevokeDislike(@RequestParam Long resourceId) {
        resourceServices.likeResourceAndRevokeDislike(resourceId);
        return ResponseEntity.ok("Like resource and revoke dislike with ID: " + resourceId);
    }

    @PostMapping("/dislikeResourceAndRevokeLike")
    public ResponseEntity<String> dislikeResourceAndRevokeLike(@RequestParam Long resourceId) {
        resourceServices.dislikeResourceAndRevokeLike(resourceId);
        return ResponseEntity.ok("Dislike resource and revoke like with ID: " + resourceId);
    }

    @GetMapping("/getUserId")
    public ResponseEntity<Long> getUserId(Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        return ResponseEntity.ok(user.getId());
    }

    @PostMapping("/setLikeToActive")
    public ResponseEntity<String> setLikeToActive(@RequestParam Long resourceId, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        Long userId = user.getId();
        //System.out.println("User ID: " + userId);
        userResourceLikeDislikeService.ChangeLikeStatusToActive(resourceId, userId);
        return ResponseEntity.ok("Like set to active with ID: " + resourceId);
    }

    @PostMapping("/setLikeToInactive")
    public ResponseEntity<String> setLikeToInactive(@RequestParam Long resourceId, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        Long userId = user.getId();
        //System.out.println("User ID: " + userId);
        userResourceLikeDislikeService.ChangeLikeStatusToInactive(resourceId, userId);
        return ResponseEntity.ok("Like set to inactive with ID: " + resourceId);
    }

    @PostMapping("/setDislikeToActive")
    public ResponseEntity<String> setDislikeToActive(@RequestParam Long resourceId, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        Long userId = user.getId();
        //System.out.println("User ID: " + userId);
        userResourceLikeDislikeService.ChangeDislikeStatusToActive(resourceId, userId);
        return ResponseEntity.ok("Dislike set to active with ID: " + resourceId);
    }


    @PostMapping("/setDislikeToInactive")
    public ResponseEntity<String> setDislikeToInactive(@RequestParam Long resourceId, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        Long userId = user.getId();
        //System.out.println("User ID: " + userId);
        userResourceLikeDislikeService.ChangeDislikeStatusToInactive(resourceId, userId);
        return ResponseEntity.ok("Dislike set to inactive with ID: " + resourceId);
    }

    @PostMapping("/setLikeToActiveAndDislikeToInactive")
    public ResponseEntity<String> setLikeToActiveAndDislikeToInactive(@RequestParam Long resourceId, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        Long userId = user.getId();
        //System.out.println("User ID: " + userId);
        userResourceLikeDislikeService.ChangeLikeStatusToActiveAndDislikeToInactive(resourceId, userId);
        return ResponseEntity.ok("Like set to active and dislike set to inactive with ID: " + resourceId);
    }

    @PostMapping("/setDislikeToActiveAndLikeToInactive")
    public ResponseEntity<String> setDislikeToActiveAndLikeToInactive(@RequestParam Long resourceId, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        Long userId = user.getId();
        //System.out.println("User ID: " + userId);
        userResourceLikeDislikeService.ChangeDislikeStatusToActiveAndLikeToInactive(resourceId, userId);
        return ResponseEntity.ok("Dislike set to active and like set to inactive with ID: " + resourceId);
    }

    @GetMapping("/getLikeStatus")
    public ResponseEntity<String> getLikeStatus(@RequestParam Long resourceId, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        Long userId = user.getId();
        //System.out.println("User ID: " + userId);
        String likeStatus = userResourceLikeDislikeService.getLikeStatus(resourceId, userId);
        System.out.println("Like status: " + likeStatus);
        return ResponseEntity.ok(likeStatus);
    }


    @GetMapping("/getDislikeStatus")
    public ResponseEntity<String> getDislikeStatus(@RequestParam Long resourceId, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        Long userId = user.getId();
        String dislikeStatus = userResourceLikeDislikeService.getDislikeStatus(resourceId, userId);
        System.out.println("Dislike status: " + dislikeStatus);
        return ResponseEntity.ok(dislikeStatus);
    }

    @GetMapping("/getLikeAndDislikeStatuses")
    public ResponseEntity<Map<String,Object>> getLikeAndDislikeStatus( Principal principal){
        Map<String, Object> response = new HashMap<>();
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        Long userId = user.getId();
        List<UserLikeAndDislikeData> likeanddislike = userResourceLikeDislikeService.getLikeAndDislikeStatus(userId);
        response.put("likeanddislike", likeanddislike);
        return ResponseEntity.ok(response);
    }

}
