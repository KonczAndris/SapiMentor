package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;


import lombok.RequiredArgsConstructor;
import lombok.experimental.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.UserRegistrationDto;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.token.ConfirmationToken;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Role;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ConfirmationTokenRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import javax.persistence.Cacheable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//megkerdezni, hogy ez miert kell!!!!!
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private UserRegistrationDto UserRegistrationDto;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

//    public UserServiceImpl(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }


    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    //ez a regisztraciohoz kell
    @Override
    public User save(UserRegistrationDto registrationDto) {
        User user = new User(registrationDto.getFirstName(),
                registrationDto.getLastName(), registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()), Arrays.asList(new Role("USER")));
        return userRepository.save(user);
    }

    public void saveUserVerificationToken(User theUser, String token){
        var verificationToken = new ConfirmationToken(token, theUser);
        confirmationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateToken(String theToken) {
        ConfirmationToken token = confirmationTokenRepository.findByToken(theToken);
        if(token == null){
            return "Invalid verification token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime()- calendar.getTime().getTime()) <= 0){
            confirmationTokenRepository.delete(token);
            userRepository.delete(user);
            return "Token already expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

//    //bejelekezesnel kell
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByEmail(username);
//        if(user == null || !user.isEnabled()) {
//            throw new UsernameNotFoundException("Invalid username or password.");
//        }
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
//    }
//
//    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
//        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
//    }



}
