package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamsDTO {
    private Long id;
    private String name;
    private String topic_name;
    private String user_name;
    private int like;
    private int dislike;
    
}
