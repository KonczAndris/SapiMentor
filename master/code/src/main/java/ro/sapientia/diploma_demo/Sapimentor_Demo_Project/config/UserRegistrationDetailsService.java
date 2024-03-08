package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Role;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserRegistrationDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    //bejelekezesnel kell
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(user == null || !user.isEnabled()) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}

