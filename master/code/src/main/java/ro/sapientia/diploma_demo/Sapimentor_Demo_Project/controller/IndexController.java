package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config.UserRegistrationDetails;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Role;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
public class IndexController {
    private final UserRepository userRepository;

    @Autowired
    public IndexController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String showIndex(Model model, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(user);

        List<String> rolesToDisplayIndex = new ArrayList<>();
        boolean hasOtherRole = false;

        for(Role role : user.getRoles()) {
            if(!role.getName().equals("USER")) {
                rolesToDisplayIndex.add(role.getName());
                hasOtherRole = true;
            }
        }

        if(!hasOtherRole) {
            rolesToDisplayIndex.add("USER");
        }

        String rolesAsString = String.join(",", rolesToDisplayIndex);
        model.addAttribute("userRolesToDisplayIndex", rolesAsString);

        byte[] indexprofileBytes = user.getProfileImage();

        if(indexprofileBytes != null){
            String indexImagesBase64 = Base64.getEncoder().encodeToString(indexprofileBytes);
            model.addAttribute("indexImagesBase64", indexImagesBase64);
        }else{
            model.addAttribute("indexImagesBase64", "");
        }

        model.addAttribute("userRegistrationDetails", userRegistrationDetails);

        return "index";
    }
}
