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
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.*;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.ExamServices;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.ResourceServices;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.TopicService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.UserExamLikeDislikeService;

import java.security.Principal;
import java.util.*;

@RequestMapping("/resources/examExamples")
@Controller
public class ExamsController {
    private final UserRepository userRepository;
    private final TopicService topicService;
    private final ResourceServices resourceServices;
    private final ExamServices examServices;
    private final UserExamLikeDislikeService userExamLikeDislikeService;

    @Autowired
    public ExamsController(UserRepository userRepository,
                           TopicService topicService,
                           ResourceServices resourceServices,
                           ExamServices examServices,
                           UserExamLikeDislikeService userExamLikeDislikeService) {
        this.userRepository = userRepository;
        this.topicService = topicService;
        this.resourceServices = resourceServices;
        this.examServices = examServices;
        this.userExamLikeDislikeService = userExamLikeDislikeService;
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

//    @GetMapping("")
//    public String showExamExamples(Model model,
//                                   Principal principal) {
//
//        showUserRolesToDisplayResources(model, principal);
//        showTopicsToDisplayResources(model, principal);
//        List<Exams> exams = examServices.getAllExams();
//
//        List<String> examImageBase64List = new ArrayList<>();
//        for (Exams exam : exams) {
//            byte[] examImageBytes = exam.getExamImage();
//            if (examImageBytes != null) {
//                String examImageBase64 = Base64.getEncoder().encodeToString(examImageBytes);
//                examImageBase64List.add(examImageBase64);
//            } else {
//                examImageBase64List.add("");
//            }
//        }
//
//        model.addAttribute("examsData", exams);
//        //System.out.println("examImageBase64List: " + examImageBase64List);
//        model.addAttribute("examImageBase64List", examImageBase64List);
////        model.addAllAttributes(Map.of(
////                "examsData", exams,
////                "examImageBase64List", examImageBase64List
////        ));
//        showProfileImageAndName(model, principal);
//
//        return "examExamples";
//    }

    @GetMapping("")
    public String showExamExamples(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        showUserRolesToDisplayResources(model, principal);
        showTopicsToDisplayResources(model, principal);

        List<Exams> exams = examServices.getExamsWithSelectedFields();
        //System.out.println("Exams: " + exams);

//        List<String> examImageBase64List = exams.stream()
//                .map(exam -> Optional.ofNullable(exam.getExamImage())
//                        .map(imageBytes -> Base64.getEncoder().encodeToString(imageBytes))
//                        .orElse(""))
//                .collect(Collectors.toList());

        model.addAllAttributes(Map.of(
                "examsData", exams
                //"examImageBase64List", examImageBase64List
        ));

        showProfileImageAndName(model, principal);

        return "examExamples";
    }


//    @GetMapping("/getexambyId")
//    public ResponseEntity<List<Object[]>> getExamImage(@RequestParam Long examId) {
//        try {
//            List<Object[]> images = examServices.getExamImage(examId);
//            return ResponseEntity.ok(images);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

    @GetMapping("/getallexamimage")
    public ResponseEntity<Map<String,Object>> getAllExamImage() {
        try {
            Map<String, Object> response = new HashMap<>();
            List<Object[]> examImageBytesList = examServices.getAllExamImageById();
            response.put("examimagesandid", examImageBytesList);
            //System.out.println("examImageBytesList: " + examImageBytesList);

            //System.out.println("examImageBase64List: " + examImageBase64List);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



//    @GetMapping("")
//    public String showExamExamples(
//            Model model,
//            Principal principal,
//            @RequestParam(name = "page", defaultValue = "0") int page,
//            @RequestParam(name = "size", defaultValue = "14") int size
//    ) {
//        showUserRolesToDisplayResources(model, principal);
//        showTopicsToDisplayResources(model, principal);
//
//        // Az adott oldalszám és méret alapján kérjük le az adatokat
//        Page<Exams> examsPage = examServices.getExamsByPageAndSize(page, size);
//        List<Exams> exams = examsPage.getContent();
//
////        // Képek Base64 kódolása
////        List<String> examImageBase64List = exams.stream()
////                .map(exam -> Optional.ofNullable(exam.getExamImage())
////                        .map(imageBytes -> Base64.getEncoder().encodeToString(imageBytes))
////                        .orElse(""))
////                .collect(Collectors.toList());
//
//        model.addAllAttributes(Map.of(
//                "examsData", exams
////                "examImageBase64List", examImageBase64List
//        ));
//
//        showProfileImageAndName(model, principal);
//
//        return "examExamples";
//    }




    @PostMapping("/uploadExams")
    public ResponseEntity<String> uploadExams(@RequestParam("image") MultipartFile image,
                                              @RequestParam("name") String name,
                                              @RequestParam("topic") String topic,
                                              Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        if (user != null) {
            String user_name = user.getFirst_Name() + " " + user.getLast_Name();
            try {
                String errorMessage = examServices.uploadExamImage(image, name, topic, user_name);
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

    @PostMapping("/like")
    public ResponseEntity<String> likeExam(@RequestParam Long examId) {
        try {
            examServices.likeExam(examId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @PostMapping("/dislike")
    public ResponseEntity<String> dislikeExam(@RequestParam Long examId) {
        try {
            examServices.dislikeExam(examId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @PostMapping("/revokelike")
    public ResponseEntity<String> revokeLikeExam(@RequestParam Long examId) {
        try {
            examServices.revokeLike(examId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @PostMapping("/revokedislike")
    public ResponseEntity<String> revokeDislikeExam(@RequestParam Long examId) {
        try {
            examServices.revokeDislike(examId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @PostMapping("/likeExamAndRevokeDislike")
    public ResponseEntity<String> likeExamAndRevokeDislike(@RequestParam Long examId) {
        try {
            examServices.likeExamAndRevokeDislike(examId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @PostMapping("/dislikeExamAndRevokeLike")
    public ResponseEntity<String> dislikeExamAndRevokeLike(@RequestParam Long examId) {
        try {
            examServices.dislikeExamAndRevokeLike(examId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @GetMapping("/getUserId")
    public ResponseEntity<Long> getUserId(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user.getId());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    @PostMapping("/setLikeToActive")
    public ResponseEntity<String> setLikeToActive(@RequestParam Long examId, Principal principal) {
        try {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            Long userId = user.getId();
            userExamLikeDislikeService.ChangeLikeStatusToActive(examId, userId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @PostMapping("/setLikeToInactive")
    public ResponseEntity<String> setLikeToInactive(@RequestParam Long examId, Principal principal) {
        try {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            Long userId = user.getId();
            userExamLikeDislikeService.ChangeLikeStatusToInactive(examId, userId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @PostMapping("/setDislikeToActive")
    public ResponseEntity<String> setDislikeToActive(@RequestParam Long examId, Principal principal) {
        try {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            Long userId = user.getId();
            userExamLikeDislikeService.ChangeDislikeStatusToActive(examId, userId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @PostMapping("/setDislikeToInactive")
    public ResponseEntity<String> setDislikeToInactive(@RequestParam Long examId, Principal principal) {
        try {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            Long userId = user.getId();
            userExamLikeDislikeService.ChangeDislikeStatusToInactive(examId, userId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @PostMapping("/setLikeToActiveAndDislikeToInactive")
    public ResponseEntity<String> setLikeToActiveAndDislikeToInactive(@RequestParam Long examId, Principal principal) {
        try {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            Long userId = user.getId();
            userExamLikeDislikeService.ChangeLikeStatusToActiveAndDislikeToInactive(examId, userId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @PostMapping("/setDislikeToActiveAndLikeToInactive")
    public ResponseEntity<String> setDislikeToActiveAndLikeToInactive(@RequestParam Long examId, Principal principal) {
        try {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            Long userId = user.getId();
            userExamLikeDislikeService.ChangeDislikeStatusToActiveAndLikeToInactive(examId, userId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @GetMapping("/getLikeStatus")
    public ResponseEntity<String> getLikeStatus(@RequestParam Long examId, Principal principal) {
        try {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            Long userId = user.getId();
            String likeStatus = userExamLikeDislikeService.getLikeStatus(examId, userId);
            return ResponseEntity.ok(likeStatus);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getDislikeStatus")
    public ResponseEntity<String> getDislikeStatus(@RequestParam Long examId, Principal principal) {
        try {
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            Long userId = user.getId();
            String dislikeStatus = userExamLikeDislikeService.getDislikeStatus(examId, userId);
            return ResponseEntity.ok(dislikeStatus);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getLikeAndDislikeStatuses")
    public ResponseEntity<Map<String,Object>> getLikeAndDislikeStatus(Principal principal) {
        try {
            Map<String, Object> response = new HashMap<>();
            String email = principal.getName();
            User user = userRepository.findByEmail(email);
            Long userId = user.getId();
            List<UserLikeAndDislikeData> likeanddislike = userExamLikeDislikeService.getLikeAndDislikeStatus(userId);
            response.put("likeanddislike", likeanddislike);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
