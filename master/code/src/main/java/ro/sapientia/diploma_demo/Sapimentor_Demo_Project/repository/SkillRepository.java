package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Skill;

import java.util.List;


public interface SkillRepository extends JpaRepository<Skill, Long> {

    @Query("SELECT s FROM Skill s ORDER BY s.topic.id ASC, s.skill")
    List<Skill> findAllByASC();
}
