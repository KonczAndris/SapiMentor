package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Skill;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Topic;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.SkillRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.TopicRepository;
import javax.transaction.Transactional;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final TopicRepository topicRepository;
    private final SkillRepository skillRepository;

    public DatabaseInitializer(TopicRepository topicRepository, SkillRepository skillRepository) {
        this.topicRepository = topicRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception{
        //System.out.println(dataAlreadyLoaded());
        if(!dataAlreadyLoaded()) {
            Topic topic1 = new Topic("Informatics");
            Topic topic2 = new Topic("Languages");
            Topic topic3 = new Topic("Engineering");

            Skill skill1 = new Skill("C", topic1);
            Skill skill2 = new Skill("C#", topic1);
            Skill skill3 = new Skill(".NET", topic1);
            Skill skill4 = new Skill("Graph", topic1);
            Skill skill5 = new Skill("Databases", topic1);
            Skill skill6 = new Skill("Algorithms", topic1);
            Skill skill7 = new Skill("System programming and operating languages", topic1);
            Skill skill8 = new Skill("OOP", topic1);

            Skill skill9 = new Skill("Terminology", topic2);
            Skill skill10 = new Skill("Specialized language", topic2);
            Skill skill11 = new Skill("Translation theory and practice", topic2);

            Skill skill12 = new Skill("Electrical measurements", topic3);
            Skill skill13 = new Skill("Digital and analogue electronics", topic3);
            Skill skill14 = new Skill("Microprocessors", topic3);

            topic1.getSkills().add(skill1);
            topic1.getSkills().add(skill2);
            topic1.getSkills().add(skill3);
            topic1.getSkills().add(skill4);
            topic1.getSkills().add(skill5);
            topic1.getSkills().add(skill6);
            topic1.getSkills().add(skill7);
            topic1.getSkills().add(skill8);

            topic2.getSkills().add(skill9);
            topic2.getSkills().add(skill10);
            topic2.getSkills().add(skill11);

            topic3.getSkills().add(skill12);
            topic3.getSkills().add(skill13);
            topic3.getSkills().add(skill14);

            topicRepository.save(topic1);
            topicRepository.save(topic2);
            topicRepository.save(topic3);
        }
    }

    // Ellenőrizzük, hogy van-e már bejegyzés a Topic táblában
    private boolean dataAlreadyLoaded() {
        return topicRepository.count() > 0;
    }
}
