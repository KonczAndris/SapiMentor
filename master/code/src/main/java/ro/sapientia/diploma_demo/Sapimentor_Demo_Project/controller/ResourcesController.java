package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResourcesController {
    @GetMapping("/resources")
    public String showResources() {
        return "resources";
    }
    @GetMapping("/resources/examexamples")
    public String showExamExamples() {
        return "examExamples";
    }
    @GetMapping("/resources/diplomatheses")
    public String showDiplomaTheses() {
        return "diplomaTheses";
    }
}
