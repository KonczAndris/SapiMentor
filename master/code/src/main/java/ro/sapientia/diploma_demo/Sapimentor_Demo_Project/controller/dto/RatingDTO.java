package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingDTO {
    private int score;

    public RatingDTO(int score) {
        this.score = score;
    }
}
