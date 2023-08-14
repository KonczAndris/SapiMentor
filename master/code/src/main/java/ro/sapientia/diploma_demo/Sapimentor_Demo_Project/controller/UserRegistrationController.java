package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.UserRegistrationDto;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.token.ConfirmationToken;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.event.RegistrationCompleteEvent;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ConfirmationTokenRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.UserService;

import javax.servlet.http.HttpServletRequest;

//megkerdezni, hogy ez miert kell
@Controller
@RequestMapping("/register")
public class UserRegistrationController {
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public UserRegistrationController(UserService userService, ApplicationEventPublisher eventPublisher, ConfirmationTokenRepository confirmationTokenRepository) {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
        this.confirmationTokenRepository = confirmationTokenRepository;
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
            System.out.println("User: " + user.getEmail() + " has been registered successfully");
            eventPublisher.publishEvent(new RegistrationCompleteEvent(user, appUrl(request)));
        } catch (DataIntegrityViolationException ex) {
            return "redirect:/register?duplicateError";
        }
        return "redirect:/register?success";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        ConfirmationToken theToken = confirmationTokenRepository.findByToken(token);
        if (theToken.getUser().isEnabled()){
            return "This account has already been verified, please, login.";
        }
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")){
            return "Email verified successfully. Now you can login to your account";
        }
        return "Invalid verification token";
    }

    public String appUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    } //ezzel a fuggvennyel tudja a regisztracio utan a linket megjeleniteni
}
