package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Role;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Data
public class UserRegistrationDetails implements UserDetails {
    private String userName;
    private String password;
    private boolean isEnabled;
    private List<GrantedAuthority> authorities;
    private List<String> roles;
    private String firstName;
    private String lastName;
    private String specialization;
    private Integer year;
    private String phoneNumber;
    private byte[] profileImage;

    public UserRegistrationDetails(User user) {
        this.userName = user.getEmail();
        this.password = user.getPassword();
        this.isEnabled = user.isEnabled();
        this.roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        this.firstName = user.getFirst_Name();
        this.lastName = user.getLast_Name();
        this.specialization = user.getSpecialization();
        this.year = user.getYear();
        this.phoneNumber = user.getPhoneNumber();
        this.profileImage = user.getProfileImage();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public List<String> getRoles() {
        return roles;
    }
}
