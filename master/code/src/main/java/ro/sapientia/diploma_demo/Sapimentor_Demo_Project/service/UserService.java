package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.UserRegistrationDto;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;

import java.util.List;

public interface UserService extends UserDetailsService{
    List<User> getUsers();
    User save(UserRegistrationDto registrationDto);

    void saveUserVerificationToken(User theUser, String verificationToken);
}
