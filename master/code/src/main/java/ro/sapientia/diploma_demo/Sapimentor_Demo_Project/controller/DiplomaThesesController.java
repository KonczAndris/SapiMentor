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
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.UserLikeAndDislikeData;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.DiplomaServices;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.TopicService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.UserDiplomaLikeDislikeService;

import java.security.Principal;
import java.util.*;

@RequestMapping("/resources/diplomaTheses")
@Controller
public class DiplomaThesesController {

    private final UserRepository userRepository;
    private final TopicService topicService;
    private final DiplomaServices diplomaServices;
    private final UserDiplomaLikeDislikeService userDiplomaLikeDislikeService;

    @Autowired
    public DiplomaThesesController(UserRepository userRepository,
                                   TopicService topicService,
                                   DiplomaServices diplomaServices,
                                   UserDiplomaLikeDislikeService userDiplomaLikeDislikeService) {
        this.userRepository = userRepository;
        this.topicService = topicService;
        this.diplomaServices = diplomaServices;
        this.userDiplomaLikeDislikeService = userDiplomaLikeDislikeService;
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
    public String showDiplomaTheses(Model model, Principal principal) {
        showUserRolesToDisplayResources(model, principal);
        showTopicsToDisplayResources(model, principal);
        showProfileImageAndName(model, principal);
        return "diplomaTheses";
    }

    @PostMapping("/uploadDiplomaTheses")
    public ResponseEntity<String> uploadDiplomaTheses(@RequestParam("pdf") MultipartFile pdf,
                                                      @RequestParam("name") String name,
                                                      @RequestParam("topic") String topic,
                                                      Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        //System.out.println("User: " + user.getEmail());

        if (user != null){
            String user_name = user.getFirst_Name() + " " + user.getLast_Name();
            try {
                String errorMessage = diplomaServices.uploadDiplomaThesesPdf(pdf, name, topic, user_name);
                System.out.println("Error message: " + errorMessage);
                if (errorMessage != null){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
                }

                return ResponseEntity.ok("Success");
            } catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The user is not logged in!");
    }

    // TODO: /like
    @PostMapping("/like")
    public ResponseEntity<String> likeDiploma(@RequestParam Long diplomaId){
        try {
            diplomaServices.likeDiploma(diplomaId);
            return ResponseEntity.ok("Success");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    // TODO: /dislike
    @PostMapping("/dislike")
    public ResponseEntity<String> dislikeDiploma(@RequestParam Long diplomaId){
        try {
            diplomaServices.dislikeDiploma(diplomaId);
            return ResponseEntity.ok("Success");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    // TODO: /revokelike
    @PostMapping("/revokelike")
    public ResponseEntity<String> revokeLike(@RequestParam Long diplomaId){
        try {
            diplomaServices.revokeLike(diplomaId);
            return ResponseEntity.ok("Success");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    // TODO: /revokedislike
    @PostMapping("/revokedislike")
    public ResponseEntity<String> revokeDislike(@RequestParam Long diplomaId){
        try {
            diplomaServices.revokeDislike(diplomaId);
            return ResponseEntity.ok("Success");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    // TODO: /likeDiplomaAndRevokeDislike
    @PostMapping("/likeDiplomaAndRevokeDislike")
    public ResponseEntity<String> likeDiplomaAndRevokeDislike(@RequestParam Long diplomaId){
        try {
            diplomaServices.likeDiplomaAndRevokeDislike(diplomaId);
            return ResponseEntity.ok("Success");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    // TODO: /dislikeDiplomaAndRevokeLike
    @PostMapping("/dislikeDiplomaAndRevokeLike")
    public ResponseEntity<String> dislikeDiplomaAndRevokeLike(@RequestParam Long diplomaId){
        try {
            diplomaServices.dislikeDiplomaAndRevokeLike(diplomaId);
            return ResponseEntity.ok("Success");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    // TODO: /setLikeToActive
    @PostMapping("/setLikeToActive")
    public ResponseEntity<String> setLikeToActive(@RequestParam Long diplomaId, Principal principal){
        try {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            Long userId = user.getId();
            userDiplomaLikeDislikeService.ChangeLikeStatusToActive(diplomaId, userId);
            return ResponseEntity.ok("Success");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    // TODO: /setLikeToInactive
    @PostMapping("/setLikeToInactive")
    public ResponseEntity<String> setLikeToInactive(@RequestParam Long diplomaId, Principal principal){
        try {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            Long userId = user.getId();
            userDiplomaLikeDislikeService.ChangeLikeStatusToInactive(diplomaId, userId);
            return ResponseEntity.ok("Success");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    // TODO: /setDislikeToActive
    @PostMapping("/setDislikeToActive")
    public ResponseEntity<String> setDislikeToActive(@RequestParam Long diplomaId, Principal principal){
        try {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            Long userId = user.getId();
            userDiplomaLikeDislikeService.ChangeDislikeStatusToActive(diplomaId, userId);
            return ResponseEntity.ok("Success");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    // TODO: /setDislikeToInactive
    @PostMapping("/setDislikeToInactive")
    public ResponseEntity<String> setDislikeToInactive(@RequestParam Long diplomaId, Principal principal){
        try {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            Long userId = user.getId();
            userDiplomaLikeDislikeService.ChangeDislikeStatusToInactive(diplomaId, userId);
            return ResponseEntity.ok("Success");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    // TODO: /setLikeToActiveAndDislikeToInactive
    @PostMapping("/setLikeToActiveAndDislikeToInactive")
    public ResponseEntity<String> setLikeToActiveAndDislikeToInactive(@RequestParam Long diplomaId, Principal principal){
        try {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            Long userId = user.getId();
            userDiplomaLikeDislikeService.ChangeLikeStatusToActiveAndDislikeToInactive(diplomaId, userId);
            return ResponseEntity.ok("Success");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }

    }

    // TODO: /setDislikeToActiveAndLikeToInactive
    @PostMapping("/setDislikeToActiveAndLikeToInactive")
    public ResponseEntity<String> setDislikeToActiveAndLikeToInactive(@RequestParam Long diplomaId, Principal principal){
        try {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            Long userId = user.getId();
            userDiplomaLikeDislikeService.ChangeDislikeStatusToActiveAndLikeToInactive(diplomaId, userId);
            return ResponseEntity.ok("Success");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }

    }

    // TODO: /getLikeStatus
    @GetMapping("/getLikeStatus")
    public ResponseEntity<String> getLikeStatus(@RequestParam Long diplomaId, Principal principal){
        try {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            Long userId = user.getId();
            String likeStatus = userDiplomaLikeDislikeService.getLikeStatus(diplomaId, userId);
            return ResponseEntity.ok(likeStatus);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }

    }

    // TODO: /getDislikeStatus
    @GetMapping("/getDislikeStatus")
    public ResponseEntity<String> getDislikeStatus(@RequestParam Long diplomaId, Principal principal){
        try {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            Long userId = user.getId();
            String dislikeStatus = userDiplomaLikeDislikeService.getDislikeStatus(diplomaId, userId);
            return ResponseEntity.ok(dislikeStatus);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }

    }

    // TODO: /getLikeAndDislikeStatues
    @GetMapping("/getLikeAndDislikeStatuses")
    public ResponseEntity<Map<String,Object>> getLikeAndDislikeStatus(Principal principal){
        try {
            Map<String, Object> response = new HashMap<>();
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            Long userId = user.getId();
            List<UserLikeAndDislikeData> likeanddislike = userDiplomaLikeDislikeService.getLikeAndDislikeStatus(userId);
            response.put("likeanddislike", likeanddislike);
            return ResponseEntity.ok(response);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

}
