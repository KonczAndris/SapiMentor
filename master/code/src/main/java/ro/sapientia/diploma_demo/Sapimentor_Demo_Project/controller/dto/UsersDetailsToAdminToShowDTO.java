package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto;

import java.time.LocalDateTime;

public class UsersDetailsToAdminToShowDTO {
    public Long user_id;
    public String first_Name;
    public String last_Name;
    public String email;
    public boolean enabled;
    public String specialization;
    public Integer year;
    public String phoneNumber;
    public String profileImage;
    public Integer status;
    public LocalDateTime online_at;
    public String modified_by;
    public LocalDateTime modified_at;
    public Integer signed_in;

    public UsersDetailsToAdminToShowDTO() {
    }

    public UsersDetailsToAdminToShowDTO(Long user_id,
                                  String first_Name,
                                  String last_Name,
                                  String email,
                                  boolean enabled,
                                  String specialization,
                                  Integer year,
                                  String phoneNumber,
                                  String profileImage,
                                  Integer status,
                                  LocalDateTime online_at,
                                  String modified_by,
                                  LocalDateTime modified_at,
                                        Integer signed_in) {
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
        this.signed_in = signed_in;
    }
}
