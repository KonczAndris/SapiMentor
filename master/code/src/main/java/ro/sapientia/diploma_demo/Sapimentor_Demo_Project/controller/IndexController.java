package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.UserRegistrationDto;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class IndexController {
//    @GetMapping("/profile")
//    public String showProfilePage() {
//        return "profile"; // This maps to the "profile.html" template
//    }
}
