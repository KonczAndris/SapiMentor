package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config.UserRegistrationDetails;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.UsersDetailsToAdminDTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.UsersDetailsToAdminToShowDTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Role;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Topic;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.TopicService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.UserService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.UserServiceImpl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final TopicService topicService;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserService userService,
                          UserRepository userRepository,
                          TopicService topicService,
                          UserServiceImpl userServiceImpl) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.topicService = topicService;
        this.userServiceImpl = userServiceImpl;
    }

    private void showUserRolesToDisplayUsers(Model model,
                                             Principal principal) {

        String email = principal.getName();

        Long userId = userRepository.findIdByEmail(email);
        Collection<Role> roles = userRepository.findRolesByUserId(userId);


        List<String> rolesToDisplayUsers = new ArrayList<>();
        boolean hasOtherRole = false;

        for (Role role : roles){
            if(!role.getName().equals("USER")){
                rolesToDisplayUsers.add(role.getName());
                hasOtherRole = true;
            }
        }

        if(!hasOtherRole){
            rolesToDisplayUsers.add("USER");
        }

        String rolesAsString = String.join(",", rolesToDisplayUsers);
        model.addAttribute("userRolesToDisplayUsers", rolesAsString);
    }

    private void showTopicsToDisplayUsers(Model model,
                                          Principal principal) {
        List<Topic> topics = topicService.getAllTopics();
        model.addAttribute("topics", topics);
    }

    private void showProfileImageAndName(Model model,
                                         Principal principal) {
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
        model.addAttribute("userIdForUsersPage", user.getId());
    }

    @Cacheable("users")
    @GetMapping("")
    public String showUsers(Model model,
                            Principal principal){
        if (principal == null) {
            return "redirect:/login";
        }
        showUserRolesToDisplayUsers(model, principal);
        showTopicsToDisplayUsers(model, principal);

        List<UsersDetailsToAdminDTO> users = userRepository.findAllUsersToAdmin(principal.getName());

        List<UsersDetailsToAdminToShowDTO> usersToShow = new ArrayList<>();
        for (UsersDetailsToAdminDTO user : users) {
            String encodedProfileImage = null;
            if (user.profileImage != null) {
                encodedProfileImage = Base64.getEncoder().encodeToString(user.profileImage);
            }

            UsersDetailsToAdminToShowDTO userToShow = new UsersDetailsToAdminToShowDTO(user.user_id,
                    user.first_Name,
                    user.last_Name,
                    user.email,
                    user.enabled,
                    user.specialization,
                    user.year,
                    user.phoneNumber,
                    encodedProfileImage,
                    user.status,
                    user.online_at,
                    user.modified_by,
                    user.modified_at,
                    user.signed_in);
            usersToShow.add(userToShow);
        }
        model.addAttribute("usersData", usersToShow);
        showProfileImageAndName(model, principal);

        return "users";
    }

    private String[] splitFilter(String filter) {
        int spaceIndex = filter.indexOf(' ');
        if (spaceIndex == -1) {
            return new String[] { filter };
        }
        String firstName = filter.substring(0, spaceIndex);
        String lastName = filter.substring(spaceIndex + 1);
        return new String[] { firstName, lastName };
    }

    @Cacheable("searchedUsers")
    @GetMapping("/search")
    public String showFilteredUsers(Model model,
                                    Principal principal,
                                    @RequestParam("filter") String filter) {
        log.info("Filter: {}", filter);
        try {
            if (principal == null) {
                return "redirect:/login";
            }
            showUserRolesToDisplayUsers(model, principal);
            showTopicsToDisplayUsers(model, principal);
            showProfileImageAndName(model, principal);
            String[] filterParts = splitFilter(filter.toLowerCase());
            List<UsersDetailsToAdminDTO> filteredUsers = null;
            if(filterParts.length >= 2) {
                filteredUsers = userRepository.findFilteredUsersToAdminFirstLast(filterParts[0], filterParts[1], principal.getName());
            }
            else {
                filteredUsers = userRepository.findFilteredUsersToAdmin(filter, principal.getName());
            }

            List<UsersDetailsToAdminToShowDTO> filteredUsersToShow = new ArrayList<>();
            for (UsersDetailsToAdminDTO user : filteredUsers) {
                String encodedProfileImage = null;
                if (user.profileImage != null) {
                    encodedProfileImage = Base64.getEncoder().encodeToString(user.profileImage);
                }

                UsersDetailsToAdminToShowDTO userToShow = new UsersDetailsToAdminToShowDTO(user.user_id,
                        user.first_Name,
                        user.last_Name,
                        user.email,
                        user.enabled,
                        user.specialization,
                        user.year,
                        user.phoneNumber,
                        encodedProfileImage,
                        user.status,
                        user.online_at,
                        user.modified_by,
                        user.modified_at
                        , user.signed_in);
                filteredUsersToShow.add(userToShow);
            }

            model.addAttribute("usersData", filteredUsersToShow);
            return "users";
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error while filtering resources!");
            return "error";
        }
    }

    @PostMapping("/modifyUser")
    public ResponseEntity<String> modifyUsers(@RequestParam("user_id") Long user_id,
                                              @RequestParam("first_Name") String first_Name,
                                              @RequestParam("last_Name") String last_Name,
                                              @RequestParam("email") String email,
                                              @RequestParam("enabled") boolean enabled,
                                              @RequestParam("specialization") String specialization,
                                              @RequestParam("year") String year,
                                              @RequestParam("phoneNumber") String phoneNumber,
                                              Principal principal) {
        String user_email = principal.getName();
        User modifing_user = userRepository.findByEmail(user_email);

        if(modifing_user != null){
            String user_name = modifing_user.getFirst_Name() + "_" + modifing_user.getLast_Name() + "_" + modifing_user.getId();
            try{
                String errorMessage = userServiceImpl.modifyUser(user_id, first_Name, last_Name, email, enabled, specialization, year, phoneNumber, user_name);
                if (errorMessage == null) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
                }
                return ResponseEntity.ok("Success");
            } catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while modifying the user!");
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The user is not logged in!");
    }

    @PostMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestParam("user_id") Long user_id
                                            , Principal principal) {
        try {
            if (principal == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("The user is not logged in!");
            }
            if (userServiceImpl.deleteSelectedUser(user_id).equals("DELETED")){
                return ResponseEntity.ok("DELETED");
            } else {
                return ResponseEntity.ok("NOTDELETED");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while deleting the user!");
        }
    }

}
