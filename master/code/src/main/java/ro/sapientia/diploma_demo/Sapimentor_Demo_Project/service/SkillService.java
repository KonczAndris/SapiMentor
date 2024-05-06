package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Skill;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.SkillRepository;

import java.util.List;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public List<Skill> getAllSkills() {
        List<Skill> skills = skillRepository.findAllByASC();
        return skills;
        //return skillRepository.findAll();
    }
}
