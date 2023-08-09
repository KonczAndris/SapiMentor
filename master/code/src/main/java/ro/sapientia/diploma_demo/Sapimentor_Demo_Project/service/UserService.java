package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.UserRegistrationDto;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;

public interface UserService extends UserDetailsService{
    User save(UserRegistrationDto registrationDto);
}
