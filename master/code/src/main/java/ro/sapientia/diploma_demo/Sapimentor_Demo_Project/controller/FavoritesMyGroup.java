package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/myGroup/favorites")
@Controller
public class FavoritesMyGroup {

    @GetMapping("")
    public String showFavoritesMyGroup() {
        return "favorites";
    }
}
