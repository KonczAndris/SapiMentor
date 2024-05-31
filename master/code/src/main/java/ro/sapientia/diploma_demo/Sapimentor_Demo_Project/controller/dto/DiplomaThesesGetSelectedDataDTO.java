package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiplomaThesesGetSelectedDataDTO {
    String name;
    String year;
    String topic_name;

    public DiplomaThesesGetSelectedDataDTO() {

    }
    public DiplomaThesesGetSelectedDataDTO(String name, String year, String topic_name) {
        this.name = name;
        this.year = year;
        this.topic_name = topic_name;
    }
}
