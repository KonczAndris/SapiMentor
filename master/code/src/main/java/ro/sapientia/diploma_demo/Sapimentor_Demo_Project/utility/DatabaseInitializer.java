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

            Skill Iskill1 = new Skill("C", topic1);
            Skill Iskill2 = new Skill("C#", topic1);
            Skill Iskill3 = new Skill(".NET", topic1);
            Skill Iskill4 = new Skill("Graph", topic1);
            Skill Iskill5 = new Skill("Databases", topic1);
            Skill Iskill6 = new Skill("Algorithms", topic1);
            Skill Iskill7 = new Skill("System programming and operating languages", topic1);
            Skill Iskill8 = new Skill("OOP", topic1);
            Skill Iskill9 = new Skill("Programming Fundamentals", topic1);
            Skill Iskill10 = new Skill("Web Development", topic1);
            Skill Iskill11 = new Skill("Python", topic1);
            Skill Iskill12 = new Skill("C++", topic1);
            Skill Iskill13 = new Skill("Java Development", topic1);
            Skill Iskill14 = new Skill("Spring Boot", topic1);
            Skill Iskill15 = new Skill("Data Structures and Algorithms", topic1);
            Skill Iskill16 = new Skill("Cybersecurity", topic1);
            Skill Iskill17 = new Skill("Machine Learning", topic1);
            Skill Iskill18 = new Skill("Artificial Intelligence", topic1);
            Skill Iskill19 = new Skill("Software Engineering", topic1);
            Skill Iskill20 = new Skill("Operating Systems", topic1);
            Skill Iskill21 = new Skill("Backend Development", topic1);
            Skill Iskill22 = new Skill("Frontend Development", topic1);
            Skill Iskill23 = new Skill("Natural Language Processing (NLP)", topic1);
            Skill Iskill24 = new Skill("Software Testing and Quality Assurance", topic1);
            Skill Iskill25 = new Skill("Data Science", topic1);
            Skill Iskill26 = new Skill("JavaScript", topic1);
            Skill Iskill27 = new Skill("Android", topic1);
            Skill Iskill28 = new Skill("Kotlin", topic1);
            Skill Iskill29 = new Skill("Haskell", topic1);
            Skill Iskill30 = new Skill("Matlab", topic1);



            Skill Lskill1 = new Skill("English", topic2);
            Skill Lskill2 = new Skill("Deutsch", topic2);
            Skill Lskill3 = new Skill("Romana", topic2);
            Skill Lskill4 = new Skill("Danish", topic2);
            Skill Lskill5 = new Skill("Terminology", topic2);
            Skill Lskill6 = new Skill("Specialized language", topic2);
            Skill Lskill7 = new Skill("Translation theory and practice", topic2);
            Skill Lskill8 = new Skill("Descriptive Grammar", topic2);
            Skill Lskill9 = new Skill("Translation theory and practice ", topic2);
            Skill Lskill10 = new Skill("Computer Translation", topic2);
            Skill Lskill11 = new Skill("Applied Linguistics", topic2);
            Skill Lskill12 = new Skill("Introduction to Interpreting", topic2);
            Skill Lskill13 = new Skill("Text Editing", topic2);
            Skill Lskill14 = new Skill("Public Relations lecture and practice", topic2);
            Skill Lskill15 = new Skill("Technical-Medical Translation", topic2);
            Skill Lskill16 = new Skill("Text type", topic2);
            Skill Lskill17 = new Skill("Audiovisual Translation", topic2);
            Skill Lskill18 = new Skill("Language practice", topic2);



            Skill Eskill1 = new Skill("Electrical measurements", topic3);
            Skill Eskill2 = new Skill("Digital and analogue electronics", topic3);
            Skill Eskill3 = new Skill("Microprocessors", topic3);
            Skill Eskill4 = new Skill("Theoretical Electrical Engineering", topic3);
            Skill Eskill5 = new Skill("Physics", topic3);
            Skill Eskill6 = new Skill("Analog Electronics", topic3);
            Skill Eskill7 = new Skill("Digital Electronics", topic3);
            Skill Eskill8 = new Skill("Measurements and Signal Converters", topic3);
            Skill Eskill9 = new Skill("Signals and Systems", topic3);
            Skill Eskill10 = new Skill("Computer Peripherals and Interfaces", topic3);
            Skill Eskill11 = new Skill("Microcontroller Systems", topic3);
            Skill Eskill12 = new Skill("Control Systems", topic3);
            Skill Eskill13 = new Skill("Computer Networks", topic3);
            Skill Eskill14 = new Skill("FPGA", topic3);
            Skill Eskill15 = new Skill("Vivado", topic3);
            Skill Eskill16 = new Skill("Matlab", topic3);
            Skill Eskill17 = new Skill("VHDL", topic3);
            Skill Eskill18 = new Skill("Real-time Systems", topic3);
            Skill Eskill19 = new Skill("Digital Signal Processing", topic3);
            Skill Eskill20 = new Skill("Model Simulation", topic3);



            topic1.getSkills().add(Iskill1);
            topic1.getSkills().add(Iskill2);
            topic1.getSkills().add(Iskill3);
            topic1.getSkills().add(Iskill4);
            topic1.getSkills().add(Iskill5);
            topic1.getSkills().add(Iskill6);
            topic1.getSkills().add(Iskill7);
            topic1.getSkills().add(Iskill8);
            topic1.getSkills().add(Iskill9);
            topic1.getSkills().add(Iskill10);
            topic1.getSkills().add(Iskill11);
            topic1.getSkills().add(Iskill12);
            topic1.getSkills().add(Iskill13);
            topic1.getSkills().add(Iskill14);
            topic1.getSkills().add(Iskill15);
            topic1.getSkills().add(Iskill16);
            topic1.getSkills().add(Iskill17);
            topic1.getSkills().add(Iskill18);
            topic1.getSkills().add(Iskill19);
            topic1.getSkills().add(Iskill20);
            topic1.getSkills().add(Iskill21);
            topic1.getSkills().add(Iskill22);
            topic1.getSkills().add(Iskill23);
            topic1.getSkills().add(Iskill24);
            topic1.getSkills().add(Iskill25);
            topic1.getSkills().add(Iskill26);
            topic1.getSkills().add(Iskill27);
            topic1.getSkills().add(Iskill28);
            topic1.getSkills().add(Iskill29);
            topic1.getSkills().add(Iskill30);




            topic2.getSkills().add(Lskill1);
            topic2.getSkills().add(Lskill2);
            topic2.getSkills().add(Lskill3);
            topic2.getSkills().add(Lskill4);
            topic2.getSkills().add(Lskill5);
            topic2.getSkills().add(Lskill6);
            topic2.getSkills().add(Lskill7);
            topic2.getSkills().add(Lskill8);
            topic2.getSkills().add(Lskill9);
            topic2.getSkills().add(Lskill10);
            topic2.getSkills().add(Lskill11);
            topic2.getSkills().add(Lskill12);
            topic2.getSkills().add(Lskill13);
            topic2.getSkills().add(Lskill14);
            topic2.getSkills().add(Lskill15);
            topic2.getSkills().add(Lskill16);
            topic2.getSkills().add(Lskill17);
            topic2.getSkills().add(Lskill18);


            topic3.getSkills().add(Eskill1);
            topic3.getSkills().add(Eskill2);
            topic3.getSkills().add(Eskill3);
            topic3.getSkills().add(Eskill4);
            topic3.getSkills().add(Eskill5);
            topic3.getSkills().add(Eskill6);
            topic3.getSkills().add(Eskill7);
            topic3.getSkills().add(Eskill8);
            topic3.getSkills().add(Eskill9);
            topic3.getSkills().add(Eskill10);
            topic3.getSkills().add(Eskill11);
            topic3.getSkills().add(Eskill12);
            topic3.getSkills().add(Eskill13);
            topic3.getSkills().add(Eskill14);
            topic3.getSkills().add(Eskill15);
            topic3.getSkills().add(Eskill16);
            topic3.getSkills().add(Eskill17);
            topic3.getSkills().add(Eskill18);
            topic3.getSkills().add(Eskill19);
            topic3.getSkills().add(Eskill20);


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
