package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.event.listener.RegistrationCompleteEventListener;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.PasswordResetTokenService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.PasswordResetTokenServiceInterface;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility.Url;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class UserLoginController {

    private final UserRepository userRepository;
    private final PasswordResetTokenServiceInterface passwordResetTokenServiceInterface;
    private final RegistrationCompleteEventListener eventListener;
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/forgotPassword")
    public String showForgotPasswordForm() {
        return "forgotPassword";
    }

    @PostMapping("/forgot-password")
    public String processFrogotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        Optional<User> user= Optional.ofNullable(userRepository.findByEmail(email));
        if (user.isEmpty() || !user.get().isEnabled()){
            return  "redirect:/forgotPassword?not_found";
        }
//        System.out.println(user.get().getEmail());
        String passwordResetToken = UUID.randomUUID().toString();
//        System.out.println(passwordResetToken);
        passwordResetTokenServiceInterface.createPasswordResetTokenForUser(user.get(), passwordResetToken);
        //send password reset verification email to the user
        String url = Url.getApplicationUrl(request)+"/newPassword?token="+passwordResetToken;
//        System.out.println(url);
        try {
            eventListener.sendPasswordResetVerificationEmail(user.get(),url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/forgotPassword?success";
    }

    @GetMapping("/newPassword")
    public String showNewPasswordForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        return "newPassword";
    }
    @PostMapping("/reset-password")
    public String resetPassword(HttpServletRequest request){
        String theToken = request.getParameter("token");
        String password = request.getParameter("password");
//        System.out.println(theToken);
//        System.out.println(password);
        String tokenVerificationResult = passwordResetTokenServiceInterface.validatePasswordResetToken(theToken);
//        System.out.println(tokenVerificationResult);
        if (!tokenVerificationResult.equalsIgnoreCase("valid")){
            return "redirect:/login?invalid_token";
        }
        Optional<User> theUser = passwordResetTokenServiceInterface.findUserByPasswordResetToken(theToken);
        if (theUser.isPresent()){
            passwordResetTokenServiceInterface.resetPassword(theUser.get(), password);
            return "redirect:/login?reset_success";
        }
        return "redirect:/newPassword?not_found";
    }
}
