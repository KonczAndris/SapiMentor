package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config.UserRegistrationDetails;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.*;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.DiplomaServices;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.TopicService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.UserDiplomaLikeDislikeService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility.UserProfileNotification;

import java.security.Principal;
import java.util.*;

@RequestMapping("/resources/diplomaTheses")
@Controller
@Slf4j
public class DiplomaThesesController {

    private final UserRepository userRepository;
    private final TopicService topicService;
    private final DiplomaServices diplomaServices;
    private final UserDiplomaLikeDislikeService userDiplomaLikeDislikeService;
    private final UserProfileNotification userProfileNotification;

    @Autowired
    public DiplomaThesesController(UserRepository userRepository,
                                   TopicService topicService,
                                   DiplomaServices diplomaServices,
                                   UserDiplomaLikeDislikeService userDiplomaLikeDislikeService,
                                   UserProfileNotification userProfileNotification) {
        this.userRepository = userRepository;
        this.topicService = topicService;
        this.diplomaServices = diplomaServices;
        this.userDiplomaLikeDislikeService = userDiplomaLikeDislikeService;
        this.userProfileNotification = userProfileNotification;
    }

    @Async
    protected void showUserRolesToDisplayResources(Model model, Principal principal){
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
        model.addAttribute("userRolesToDisplayResources", rolesAsString);
    }

    @Async
    protected void showTopicsToDisplayResources(Model model, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        // Itt lekérem a témákat a service segítségével
        List<Topic> topics = topicService.getAllTopics();
        //System.out.println("Topics: " + topics);
        model.addAttribute("topics", topics);
    }

    @Async
    protected void showProfileImageAndName(Model model, Principal principal){
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
        model.addAttribute("diplomathesesUserId", user.getId());
    }

    @GetMapping("")
    public String showDiplomaTheses(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        showUserRolesToDisplayResources(model, principal);
        showTopicsToDisplayResources(model, principal);
        showProfileImageAndName(model, principal);

        List<Diploma_Theses> diplomaTheses = diplomaServices.getAllDiplomaThesesWithSelectedFields();
        model.addAllAttributes(Map.of(
                "diplomaThesesData", diplomaTheses
        ));
        return "diplomaTheses";
    }

    ////////// Kereses nev alapjan ///////////////////////////////
    @GetMapping("/filteredByName")
    public String showFilteredByNameDiplomaTheses(Model model,
                                          Principal principal,
                                          @RequestParam("name") String name,
                                          @RequestParam("selectedTopics") String[] selectedValues) {

        try {
            if (principal == null) {
                return "redirect:/login";
            }
            showUserRolesToDisplayResources(model, principal);
            showTopicsToDisplayResources(model, principal);
            showProfileImageAndName(model, principal);
            List<Diploma_Theses> filteredDiplomaThesesByName = diplomaServices.getAllFilteredDiplomaThesesByNameWithSelectedFields(model, name, selectedValues);
            model.addAllAttributes(Map.of(
                    "diplomaThesesData", filteredDiplomaThesesByName
            ));
            return "diplomaTheses";
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error while filtering diploma theses by name.");
            return "error";
        }
    }


    ////////// Kereses kulcsszo alapjan ///////////////////////////////
    @GetMapping("/filteredByKeyword")
    public String showFilteredByKeywordDiplomaTheses(Model model,
                                          Principal principal,
                                          @RequestParam("keyword") String keyword,
                                          @RequestParam("selectedTopics") String[] selectedValues) {

        try {
            if (principal == null) {
                return "redirect:/login";
            }
            showUserRolesToDisplayResources(model, principal);
            showTopicsToDisplayResources(model, principal);
            showProfileImageAndName(model, principal);
            List<Diploma_Theses> filteredDiplomaThesesByKeyword = diplomaServices.getAllFilteredDiplomaThesesByKeywordWithSelectedFields(model, keyword, selectedValues);
            model.addAllAttributes(Map.of(
                    "diplomaThesesData", filteredDiplomaThesesByKeyword
            ));
            return "diplomaTheses";
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error while filtering diploma theses by keyword.");
            return "error";
        }
    }

    // diplomamunkak lekerdezese
    @GetMapping("/getdiplomabyid")
    public ResponseEntity<List<Object[]>> getDiplomaById(@RequestParam Long diplomaId) {
        try {
            List<Object[]> diplomas = diplomaServices.getDiplomaById(diplomaId);
            return ResponseEntity.ok(diplomas);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/gethu_abstractbyid")
    public ResponseEntity<List<Object[]>> getHU_AbstractById(@RequestParam Long diplomaId) {
        try {
            List<Object[]> hu_abstract = diplomaServices.getHU_AbstractById(diplomaId);
            return ResponseEntity.ok(hu_abstract);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/geten_abstractbyid")
    public ResponseEntity<List<Object[]>> getEN_AbstractById(@RequestParam Long diplomaId) {
        try {
            List<Object[]> en_abstract = diplomaServices.getEN_AbstractById(diplomaId);
            return ResponseEntity.ok(en_abstract);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    // diplomamunkak letoltesenek megvalositasa
    @GetMapping("/downloadDiplomaThese")
    public ResponseEntity<byte[]> downloadDiplomaTheses(@RequestParam Long diplomaId) {
        try {
            Diploma_Theses diploma_theses = diplomaServices.downloadDiplomaThesePdf(diplomaId);
            String filename = diploma_theses.getName();
            if(!filename.toLowerCase().endsWith(".pdf")){
                filename = filename + "_" + diploma_theses.getYear();
                filename += ".pdf";
            }
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(diploma_theses.getDiploma_theses_file());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    //////////////////////// ez kell a tobbihez is /////////////////////////////////////////////////////////////////
    @PostMapping("/modifyDiplomaTheses")
    public ResponseEntity<String> modifyDiplomaTheses(@RequestParam("name") String name,
                                                      @RequestParam("topic") String topic,
                                                      @RequestParam("year") String year,
                                                      @RequestParam("diploma_id") Long diploma_id,
                                                      Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        if (user != null){
            String user_name = user.getFirst_Name() + " " + user.getLast_Name();
            try {
                String errorMessage = diplomaServices.modifyDiplomaThesesPdf(name, topic, user_name, year, diploma_id);
                if (errorMessage != null){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
                }

                return ResponseEntity.ok("Success");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while modifying the diploma thesis.");
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The user is not logged in!");
    }


    //////////////////////// ez kell a tobbihez is /////////////////////////////////////////////////////////////////
    @PostMapping("/deleteDiplomaTheses")
    public ResponseEntity<String> deleteDiplomaTheses(@RequestParam("diploma_id") Long diploma_id,
                                                      Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        if (user != null){
            try {
                String errorMessage = diplomaServices.deleteDiplomaThesesPdf(diploma_id);
                if (errorMessage != null){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
                }

                return ResponseEntity.ok("Success");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while deleting the diploma thesis.");
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The user is not logged in!");
    }

    @PostMapping("/uploadDiplomaTheses")
    public ResponseEntity<String> uploadDiplomaTheses(@RequestParam("pdf") MultipartFile pdf,
                                                      @RequestParam("name") String name,
                                                      @RequestParam("topic") String topic,
                                                      @RequestParam("year") String year,
                                                      Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        if (user != null){
            String user_name = user.getFirst_Name() + " " + user.getLast_Name();
            Long user_id = user.getId();
            try {
                String errorMessage = diplomaServices.uploadDiplomaThesesPdf(pdf, name, topic, user_name, year, user_id);
                if (errorMessage != null){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
                }

                return ResponseEntity.ok("Success");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while uploading the diploma thesis.");
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The user is not logged in!");
    }


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

    @GetMapping("/getLikeAndDislikeStatuses")
    public ResponseEntity<Map<String,Object>> getLikeAndDislikeStatus(Principal principal){
        try {
            Map<String, Object> response = new HashMap<>();
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            Long userId = user.getId();
            List<UserLikeAndDislikeData> likeanddislike = userDiplomaLikeDislikeService.getLikeAndDislikeStatus(userId);
            //System.out.println("Like and dislike: " + likeanddislike);
            response.put("likeanddislike", likeanddislike);
            return ResponseEntity.ok(response);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getDiplomaThesesProfileNotificationStatus")
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
