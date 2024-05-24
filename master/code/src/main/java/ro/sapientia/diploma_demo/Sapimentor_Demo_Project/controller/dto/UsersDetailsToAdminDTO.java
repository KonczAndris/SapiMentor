package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UsersDetailsToAdminDTO {
    public Long user_id;
    public String first_Name;
    public String last_Name;
    public String email;
    public boolean enabled;
    public String specialization;
    public Integer year;
    public String phoneNumber;
    public byte[] profileImage;
    public Integer status;
    public LocalDateTime online_at;
    public String modified_by;
    public LocalDateTime modified_at;

    public UsersDetailsToAdminDTO() {
    }

    public UsersDetailsToAdminDTO(Long user_id,
                                  String first_Name,
                                  String last_Name,
                                  String email,
                                  boolean enabled,
                                  String specialization,
                                  Integer year,
                                  String phoneNumber,
                                  byte[] profileImage,
                                  Integer status,
                                  LocalDateTime online_at,
                                  String modified_by,
                                  LocalDateTime modified_at) {
        this.user_id = user_id;
        this.first_Name = first_Name;
        this.last_Name = last_Name;
        this.email = email;
        this.enabled = enabled;
        this.specialization = specialization;
        this.year = year;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
        this.status = status;
        this.online_at = online_at;
        this.modified_by = modified_by;
        this.modified_at = modified_at;
    }
}
