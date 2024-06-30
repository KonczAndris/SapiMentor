package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyGroupProfileDetailDTO {
    private String firstName;
    private String lastName;
    private Long id;

    public MyGroupProfileDetailDTO() {
    }

    public MyGroupProfileDetailDTO(String firstName,
                                   String lastName,
                                   Long id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }



}
