package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility.UserProfileNotification;

import java.security.Principal;
import java.util.*;

@RequestMapping("/resources")
@Controller
@Slf4j
public class LinksController {
    private final UserRepository userRepository;
    private final TopicService topicService;
    private final ResourcesRepository resourcesRepository;

    private final ResourceServices resourceServices;
    private final SseController sseController;
    private final VirusTotalService virusTotalService;
    private final UserResourceLikeDislikeService userResourceLikeDislikeService;
    private final UserProfileNotification userProfileNotification;


    @Autowired
    public LinksController(UserRepository userRepository,
                           TopicService topicService,
                           ResourcesRepository resourcesRepository,
                           ResourceServices resourceServices,
                           SseController sseController,
                           VirusTotalService virusTotalService,
                           UserResourceLikeDislikeService userResourceLikeDislikeService,
                           UserProfileNotification userProfileNotification) {
        this.userRepository = userRepository;
        this.topicService = topicService;
        this.resourcesRepository = resourcesRepository;
        this.resourceServices = resourceServices;
        this.sseController = sseController;
        this.virusTotalService = virusTotalService;
        this.userResourceLikeDislikeService = userResourceLikeDislikeService;
        this.userProfileNotification = userProfileNotification;
    }


    // TODO: optimize this method
    private void showUserRolesToDisplayResources(Model model, Principal principal){

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
        model.addAttribute("userRolesToDisplayResources", rolesAsString);
    }

    private void showTopicsToDisplayResources(Model model){
        // Itt lekérem a témákat a service segítségével
        List<Topic> topics = topicService.getAllTopics();
        model.addAttribute("topics", topics);
    }

    private void showProfileImageAndName(Model model, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(user);

        byte[] profileImage = user.getProfileImage();
        if(profileImage != null){
            String profileImageBase64 = Base64.getEncoder().encodeToString(profileImage);
            model.addAttribute("profileImageBase64", profileImageBase64);
        } else {
            model.addAttribute("profileImageBase64", "");
        }

        model.addAttribute("userRegistrationDetails", userRegistrationDetails);
        model.addAttribute("linksUserId", user.getId());
    }

    @GetMapping("")
    public String showResources(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        showUserRolesToDisplayResources(model, principal);
        showTopicsToDisplayResources(model);
        List<Resources> resources = resourceServices.getAllResources();
        //System.out.println("Resources: " + resources);
        model.addAttribute("resourcesData", resources);
        showProfileImageAndName(model, principal);

        return "resources";
    }

    @GetMapping("/filtered")
    public String showFilteredResources(Model model,
                                      Principal principal,
                                      @RequestParam("filter") String filter,
                                      @RequestParam("selectedTopics") String[] selectedValues) {
        try {
//            System.out.println("Filter: " + filter);
//            System.out.println("Selected values: " + Arrays.toString(selectedValues));
            if (principal == null) {
                return "redirect:/login";
            }
            showUserRolesToDisplayResources(model, principal);
            showTopicsToDisplayResources(model);
            List<Resources> resourcesFiltered = resourceServices.getAllResourcesByFilter(model, filter, selectedValues);
            model.addAttribute("resourcesData", resourcesFiltered);
            showProfileImageAndName(model, principal);
            return "resources";
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error while filtering resources!");
            return "error";
        }
    }



    //////////////////////// DELETE /////////////////////////////////////////////////////////////////
    @PostMapping("/deleteResources")
    public ResponseEntity<String> deleteResources(@RequestParam("link_id") Long link_id,
                                                  Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        if(user != null){
            try{
                String errorMessage = resourceServices.deleteResources(link_id );
                if (errorMessage != null) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
                }
                return ResponseEntity.ok("Success");
            } catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while deleting the resource!");
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The user is not logged in!");
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////


    //////////////////////// MODIFY /////////////////////////////////////////////////////////////////
    @PostMapping("/modifyResources")
    public ResponseEntity<String> modifyResources(@RequestParam("name") String name,
                                                  @RequestParam("topic") String topic,
                                                  @RequestParam("link_id") Long link_id,
                                                  Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        if(user != null){
            String user_name = user.getFirst_Name() + " " + user.getLast_Name();
            try{
                String errorMessage = resourceServices.modifyResources(name, topic, user_name, link_id );
                if (errorMessage != null) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
                }
                return ResponseEntity.ok("Success");
            } catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while modifying the resource!");
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The user is not logged in!");
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////

    @PostMapping("/uploadResources")
    public ResponseEntity<String> uploadResources(String resourcesUploadDataItems,
                                                  Principal principal){
        //ez a objectMapper a json stringet alakitja at objektumokka
        ObjectMapper objectMapper = new ObjectMapper();
        String email = principal.getName();

        // Uj megoldas (probalkozas)
        Long user_id = userRepository.findIdByEmail(email);
        String User_name = userRepository.findNameById(user_id);
        String Full_User_Name = User_name.replace(",", " ");
        try{
            // JSON string deszerializálása objektumokká
            Resources[] resources_dataItems = objectMapper.readValue(resourcesUploadDataItems, Resources[].class);

            String answer = resourceServices.processAndSaveResources(resources_dataItems, Full_User_Name, user_id);

            if(answer.equals("Success")){
                return ResponseEntity.ok("Success");
            } else if (answer.equals("NotSafe")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NotSafe");
            } else if (answer.equals("MaliciousContent")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("MaliciousContent");
            } else if (answer.equals("InvalidLink")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("InvalidLink");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
            }

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
        //System.out.println("Like status: " + likeStatus);
        return ResponseEntity.ok(likeStatus);
    }


    @GetMapping("/getDislikeStatus")
    public ResponseEntity<String> getDislikeStatus(@RequestParam Long resourceId, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        Long userId = user.getId();
        String dislikeStatus = userResourceLikeDislikeService.getDislikeStatus(resourceId, userId);
        //System.out.println("Dislike status: " + dislikeStatus);
        return ResponseEntity.ok(dislikeStatus);
    }

    @GetMapping("/getLikeAndDislikeStatuses")
    public ResponseEntity<Map<String,Object>> getLikeAndDislikeStatus( Principal principal){
        try {
            Map<String, Object> response = new HashMap<>();
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            Long userId = user.getId();
            List<UserLikeAndDislikeData> likeanddislike = userResourceLikeDislikeService.getLikeAndDislikeStatus(userId);
            response.put("likeanddislike", likeanddislike);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getLinksProfileNotificationStatus")
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
