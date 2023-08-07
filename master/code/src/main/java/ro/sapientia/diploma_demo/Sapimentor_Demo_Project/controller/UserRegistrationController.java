package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.UserRegistrationDto;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.UserService;

//megkerdezni, hogy ez miert kell
@Controller
@RequestMapping("/registration")
public class UserRegistrationController {
    private UserService userService;

    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    //fontos lepes a regisztraciohoz
    @ModelAttribute("user") //ezzel a userrel tudja a registration.html-ben a formot kitolteni
    public UserRegistrationDto userRegistrationDto(){
        return new UserRegistrationDto();
    }

    //ezzel a getmappinggel a registration.html-t hivja meg
    @GetMapping
    public String showRegistrationForm(){
        return "registration";
    }

    //ezzel a postmappinggel a registration.html-ben levo formot tolti ki
    //es ha sikeres a regisztracio, akkor megjelenik a message
    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto){
        userService.save(registrationDto);
        return "redirect:/registration?success";
    }
}
