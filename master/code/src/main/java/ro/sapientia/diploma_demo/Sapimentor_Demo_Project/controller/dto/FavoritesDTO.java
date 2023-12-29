package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoritesDTO {
    private Long id;
    private String email;
    private String fullName;
    private Integer status;
    private String favoriteProfileImageBase64;
}
