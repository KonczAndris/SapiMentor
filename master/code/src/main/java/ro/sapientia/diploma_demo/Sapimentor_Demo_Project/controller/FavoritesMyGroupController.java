package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config.UserRegistrationDetails;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.FavoritesService;

import java.security.Principal;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/myGroup/favorites")
@Controller
public class FavoritesMyGroupController {

    private final UserRepository userRepository;
    private final FavoritesService favoritesService;

    @Autowired
    public FavoritesMyGroupController(UserRepository userRepository,
                                      FavoritesService favoritesService) {
        this.userRepository = userRepository;
        this.favoritesService = favoritesService;
    }

    //TODO: Implement showProfileImageAndName
    private void showProfileImageAndName(Model model, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(user);

        byte[] profileImage = user.getProfileImage();
        if (profileImage != null) {
            String profileImageBase64 = Base64.getEncoder().encodeToString(profileImage);
            model.addAttribute("profileImageBase64", profileImageBase64);
        } else {
            model.addAttribute("profileImageBase64", "");
        }

        model.addAttribute("userRegistrationDetails", userRegistrationDetails);
        model.addAttribute("userId", user.getId());
    }

    @GetMapping("")
    public String showFavoritesMyGroup(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        showProfileImageAndName(model, principal);
        favoritesService.showAllFavorites(model, principal);

        return "favorites";
    }

    @GetMapping("/getSenderUserImg")
    public ResponseEntity<Map<String,Object>> getSenderUserImg(@RequestParam("userId") Long userId) {
        //System.out.println("UserId: " + userId);
        try {
            Map<String, Object> response = new HashMap<>();
            List<Object[]> selectedUserImg = favoritesService.getSenderUserImg(userId);
            response.put("selectedUserImg", selectedUserImg);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
