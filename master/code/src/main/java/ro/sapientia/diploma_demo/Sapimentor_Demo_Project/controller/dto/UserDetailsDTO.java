package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto;

import lombok.Getter;
import lombok.Setter;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;

@Getter
@Setter
public class UserDetailsDTO {
    private String userName;
    private String firstName;
    private String lastName;
    private String specialization;
    private Integer year;
    private String phoneNumber;
    private byte[] profileImage;

    public UserDetailsDTO(User user) {
        this.userName = user.getEmail();
        this.firstName = user.getFirst_Name();
        this.lastName = user.getLast_Name();
        this.specialization = user.getSpecialization();
        this.year = user.getYear();
        this.phoneNumber = user.getPhoneNumber();
        this.profileImage = user.getProfileImage();
    }

    public String toString() {
        return "UserDetailsDTO(userName=" + userName
                + ", firstName=" + firstName
                + ", lastName=" + lastName
                + ", specialization=" + specialization
                + ", year=" + year
                + ", phoneNumber=" + phoneNumber;
    }
}
