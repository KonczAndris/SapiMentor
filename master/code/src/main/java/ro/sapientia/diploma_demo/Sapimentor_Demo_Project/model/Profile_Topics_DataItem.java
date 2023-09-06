package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Profile_Topics_DataItem {
    private String topic;
    private List<String> skills;

    public Profile_Topics_DataItem(String topic, List<String> skills) {
        this.topic = topic;
        this.skills = skills;
    }
}
