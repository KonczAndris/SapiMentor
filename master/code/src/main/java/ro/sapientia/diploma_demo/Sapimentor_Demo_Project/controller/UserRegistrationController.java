package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.UserRegistrationDto;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.event.RegistrationCompleteEvent;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.UserService;

import javax.servlet.http.HttpServletRequest;

//megkerdezni, hogy ez miert kell
@Controller
@RequestMapping("/register")
public class UserRegistrationController {
    private UserService userService;
    private ApplicationEventPublisher eventPublisher;

    public UserRegistrationController(UserService userService) {
        this.userService = userService;
    }

    //fontos lepes a regisztraciohoz
    @ModelAttribute("user") //ezzel a userrel tudja a register.html-ben a formot kitolteni
    public UserRegistrationDto userRegistrationDto(){
        return new UserRegistrationDto();
    }

    //ezzel a getmappinggel a registration.html-t hivja meg
    @GetMapping
    public String showRegistrationForm(){
        return "register";
    }

    //ezzel a postmappinggel a register.html-ben levo formot tolti ki
    //es ha sikeres a regisztracio, akkor megjelenik a message
    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto,
                                      final HttpServletRequest request){
        try {
            User user = userService.save(registrationDto);
            eventPublisher.publishEvent(new RegistrationCompleteEvent(user, appUrl(request)));
        } catch (DataIntegrityViolationException ex) {
            return "redirect:/register?duplicateError";
        }
        return "redirect:/register?success";
    }

    public String appUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    } //ezzel a fuggvennyel tudja a regisztracio utan a linket megjeleniteni
}
