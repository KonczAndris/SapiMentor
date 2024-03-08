package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Profile_Topics_DataItem {
    private String id;
    private String topic;
    private List<String> skills;

    public Profile_Topics_DataItem(String topic, List<String> skills, String id) {
        this.topic = topic;
        this.skills = skills;
        this.id = id;
    }

    public Profile_Topics_DataItem() {
    }
}
