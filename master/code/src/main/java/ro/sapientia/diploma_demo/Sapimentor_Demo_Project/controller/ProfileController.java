package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config.UserRegistrationDetails;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.*;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.*;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.RatingService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.TopicService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.UserServiceImpl;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;

@Controller
public class ProfileController {
    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final TopicService topicService;
    private final SkillRepository skillRepository;
    private final TopicRepository topicRepository;
    private final ProfileTopicsRepository profileTopicsRepository;
    private final RatingService ratingService;
    private final RoleRepository roleRepository;


    @Autowired
    public ProfileController(UserRepository userRepository,
                             UserServiceImpl userService,
                             TopicService topicService,
                             SkillRepository skillRepository,
                             TopicRepository topicRepository,
                             ProfileTopicsRepository profileTopicsRepository,
                             RatingService ratingService,
                             RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.topicService = topicService;
        this.skillRepository = skillRepository;
        this.topicRepository = topicRepository;
        this.profileTopicsRepository = profileTopicsRepository;
        this.ratingService = ratingService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal,
                              @RequestParam(value = "errorMessage", required = false) String errorMessage,
                              @RequestParam(value = "selectedTopic", required = false) String selectedTopic) {
        if (principal == null) {
            return "redirect:/login";
        }
        String email = principal.getName(); // Bejelentkezett felhasználó neve

        // Felhasználó lekérdezése az adatbázisból a felhasználónév alapján
        User user = userRepository.findByEmail(email);

        UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(user);
        System.out.println("UJ ERTEK" + userRegistrationDetails);

        List<String> rolesToDisplay = new ArrayList<>();
        boolean hasOtherRoles = false;

        for (Role role : user.getRoles()) {
            if (!role.getName().equals("USER")) {
                rolesToDisplay.add(role.getName());
                hasOtherRoles = true;
            }
        }

        if (!hasOtherRoles) {
            rolesToDisplay.add("USER");
        }

        String rolesAsString = String.join(", ", rolesToDisplay);
        model.addAttribute("userRolesToDisplay", rolesAsString);


        // Itt lekérem a témákat a service segítségével
        List<Topic> topics = topicService.getAllTopics();

        List<Profile_Topics> userTopics = profileTopicsRepository.findByUserId(user.getId());

        if (!userTopics.isEmpty()) {
            //System.out.println("User topics: " + userTopics);
            //System.out.println("User topics: " + userTopics.get(0).getTags());
            model.addAttribute("userTopics", userTopics);
        }


        //Profil kep megjelenitese
        // A profil kép byte tömbje
        byte[] profileImageBytes = user.getProfileImage();
        // A profil kép Base64 kódolása

        //A regi verzional ez a sor nem kell !!!!
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }

        if (profileImageBytes != null) {
            String profileImageBase64 = Base64.getEncoder().encodeToString(profileImageBytes);
            model.addAttribute("profileImageBase64", profileImageBase64);
        } else {
            model.addAttribute("profileImageBase64", "");
        }
        // A témákat hozzáadod a modellhez
        model.addAttribute("topics", topics);
        model.addAttribute("userRegistrationDetails", userRegistrationDetails);

        Map<String, Double> ratingData = ratingService.getAverageRating(user.getId());
        double average = ratingData.get("average");
        int count = ratingData.get("count").intValue();
        model.addAttribute("averageOfRating", average);
        model.addAttribute("countOfRating", count);

        return "profile";
    }



    @PostMapping("/profile/save")
    public String saveProfileData(@RequestParam("firstName") String firstName,
                                  @RequestParam("lastName") String lastName,
                                  @RequestParam("specialization") String specialization,
                                  @RequestParam("year") Integer year,
                                  @RequestParam("phoneNumber") String phoneNumber,
                                  Principal principal) {
        // Bejelentkezett felhasználó neve
        String email = principal.getName(); // Bejelentkezett felhasználó neve
        //System.out.println(phoneNumber);



                // Adatok mentése a szolgáltatás segítségével
                userService.saveProfileData(email,firstName, lastName, specialization, year, phoneNumber);

        return "redirect:/profile";// Visszatérés a profil oldalra
    }


//    @PostMapping("/upload-profile-image")
//    public String uploadProfileImage(final @RequestParam("image") MultipartFile image,
//                                     Principal principal,
//                                     Model model,
//                                     HttpServletResponse response) {
//        String username = principal.getName(); // Aktuális felhasználó neve
//
//        userService.uploadProfileImage(username, image);
//
//        // Frissítsük a felhasználó adatait a modellben
//        User user = userRepository.findByEmail(username);
//        UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(user);
//        System.out.println("IGENJASFJASHKF" + userRegistrationDetails);
//        model.addAttribute("userRegistrationDetails", userRegistrationDetails);
//
//        return "redirect:/profile"; // Visszairányítás a profil oldalra
//    }

    @PostMapping("/upload-profile-image")
    public String uploadProfileImage(final @RequestParam("image") MultipartFile image,
                                     Principal principal,
                                     Model model) {
        String username = principal.getName();

        //A regi verzional ez a sor es az if nem kell !!!!
        String errorMessage = userService.uploadProfileImage(username, image);
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            return "redirect:/profile?errorMessage=" + UriUtils.encode(errorMessage, StandardCharsets.UTF_8);
        }

        userService.uploadProfileImage(username, image);
        User user = userRepository.findByEmail(username);
        UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(user);
        model.addAttribute("userRegistrationDetails", userRegistrationDetails);

        //System.out.println("User details after upload: " + userRegistrationDetails);
        return "redirect:/profile";
    }


    @GetMapping("/getSkills")
    @ResponseBody
    public List<Skill> getSkillsByTopic(@RequestParam String selectedTopic) {
        //System.out.println("Selected topic: " + selectedTopic);
        return topicService.getSkillsByTopic(selectedTopic);
    }

    @GetMapping("/getUserTopicsAndSkills")
    @ResponseBody
    public List<Profile_Topics> getUserTopicsAndSkills(Principal principal) {
        // Bejelentkezett felhasználó neve
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        List<Profile_Topics> userTopicsAndSkills = profileTopicsRepository.findByUserId(user.getId());
        //System.out.println("User topics: " + userTopics);
        return userTopicsAndSkills;
    }



    //holnap innen folytatni es megcsinalni a mentest plusz  megjeleniteni az adatokat a /profile oldalon
    @PostMapping("/saveProfileTopics")
    public ResponseEntity<String> saveProfileTopics(String profileTopicsDataItems,
                                    Principal principal) {
        //ez a objectMapper a json stringet alakitja at objektumokka
        ObjectMapper objectMapper = new ObjectMapper();
        //System.out.println("HELLO");
//        System.out.println("Profile topics data items: " + profileTopicsDataItems);
        try {
            // JSON string deszerializálása objektumokká
            Profile_Topics_DataItem[] dataItems = objectMapper.readValue(profileTopicsDataItems, Profile_Topics_DataItem[].class);
//            System.out.println("Data items: " + dataItems);

            for (Profile_Topics_DataItem dataItem : dataItems) {
                String id = dataItem.getId();
                String topic = dataItem.getTopic();
                List<String> skills = dataItem.getSkills();
//                System.out.println("Id: " + id);
//                System.out.println("Topic: " + topic);
//                System.out.println("Skills: " + skills);

                if(!id.equals("")){
                    //Frissiti az adatokat a Profile_Topics entitasban
                    //Es frissiti az adatokat az adatbazisban is
                    //System.out.println("ID NEM NULL");
                    Profile_Topics existingProfileTopic = profileTopicsRepository.findById(Long.parseLong(id)).orElse(null);
                    if(existingProfileTopic != null){
                        //System.out.println("Existing profile topic: " + existingProfileTopic);
                        existingProfileTopic.setTopic(topic);
                        existingProfileTopic.setTags(skills);
                        profileTopicsRepository.save(existingProfileTopic);
                    }

                }else{
                    //System.out.println("ID NULL");
                    // Mentsd el az adatokat a Profile_Topics entitásban
                    Profile_Topics profileTopics = new Profile_Topics();
                    profileTopics.setTopic(topic);
                    profileTopics.setTags(skills);
                    //System.out.println("Profile topics: " + profileTopics.getTopic());

                    // Hozd létre az összekapcsolást a felhasználóval (amelyet a Principal-ból kaphatsz meg)
                    // Bejelentkezett felhasználó neve
                    String email = principal.getName();
                    User user = userRepository.findByEmail(email);
                    profileTopics.setUser(user);

                    // Mentsd el a Profile_Topics entitást az adatbázisban
                    profileTopicsRepository.save(profileTopics);
                }
            }
            return ResponseEntity.ok("Success");
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }

    
    @CrossOrigin(origins = "http://localhost:8080") // Engedélyezi a CORS-t csak a http://localhost:8080 eredetű kérések számára
    @PostMapping("/deleteTopicAndSkills")
    public ResponseEntity<String> deleteTopicAndSkills(@RequestParam("topicId") Long topicId) {
        try {
            Profile_Topics topicToDelete = profileTopicsRepository.findById(topicId).orElse(null);
            if(topicToDelete != null){
                profileTopicsRepository.delete(topicToDelete);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("A témát nem találtuk.");
            }

            //System.out.println("Topic id " + topicId + " torolve");
            return ResponseEntity.ok("Sikeres törlés");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Hiba történt a törlés során");
        }
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/updateUserRoleStatus")
    public ResponseEntity<String> updateUserRoleStatus(String selectedRole,
                                                       Principal principal) throws JsonProcessingException {
        System.out.println("Selected role: " + selectedRole);
        String email = principal.getName();
        Long userId = userRepository.findByEmail(email).getId();

        if(!userService.isAtLeastSecondYear(userId)){
            //System.out.println("Nem masodik ev");
            Map<String, String> responsee = new HashMap<>();
            responsee.put("message", "Nem masod eves legalabb!"); // Ezt az üzenetet jelenítheted meg a kliensoldalon
            return ResponseEntity.ok(new ObjectMapper().writeValueAsString(responsee));
        }else{
            User user = userRepository.findByEmail(email);
            if (selectedRole.length() > 6) {
                String[] rolesArray = selectedRole.split(",");
                for (String role : rolesArray) {
                    Role newRole = new Role(role.trim());
                    user.addRole(newRole);
                    System.out.println("New role added: " + newRole);
                }
            } else if (selectedRole.length() == 6 ) {
                Role newRole = new Role(selectedRole);
                user.addRole(newRole);
            }

            userRepository.save(user);
        }

        // Az eredmény JSON objektum létrehozása
         Map<String, String> response = new HashMap<>();
         response.put("message", "Szerep módosítva!"); // Ezt az üzenetet jelenítheted meg a kliensoldalon

        // JSON objektum visszaadása a ResponseEntity segítségével
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(response));
    }

    @Transactional
    @PostMapping("/deleteUserRoleStatus")
    public ResponseEntity<String> deleteUserRoleStatus(String selectedRoleToDelete, Principal principal)
            throws JsonProcessingException {
        System.out.println("Selected role for delete: " + selectedRoleToDelete);
        String email = principal.getName();
        Long userId = userRepository.findIdByEmail(email);

        User user = userRepository.findByEmail(email);
        List<Integer> removedRoles = new ArrayList<>();
        if (selectedRoleToDelete.length() > 6) {
            String[] rolesArray = selectedRoleToDelete.split(",");
            for (String role : rolesArray) {
                String removableRole = role.trim();
                // Törölje el a kiválasztott szerepet a User objektumból
                removedRoles.addAll(user.removeRoleFromUser(user, removableRole));
            }
            System.out.println("Removed role1: " + removedRoles);
        } else if (selectedRoleToDelete.length() == 6) {
            // Ha egy szerep van kiválasztva, akkor azt is törölje
            removedRoles.addAll(user.removeRoleFromUser(user, selectedRoleToDelete.trim()));
            System.out.println("Removed roles2: " + removedRoles);
        }

        userRepository.save(user);

        // Most a removedRoles listában tárolt szerepekhez férhetsz hozzá
        for (Integer removedRole : removedRoles) {
            System.out.println("Removed role: " + removedRole);
            roleRepository.updateRolesStatusByIdCustom(removedRole);
        }

        // Az eredmény JSON objektum létrehozása
        Map<String, String> response = new HashMap<>();
        response.put("message", "Szerep torolve!"); // Ezt az üzenetet jelenítheted meg a kliensoldalon

        // JSON objektum visszaadása a ResponseEntity segítségével
        return ResponseEntity.ok(new ObjectMapper().writeValueAsString(response));
    }

}



