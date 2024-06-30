package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Skill;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Topic;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.TopicRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.Topics_CommentRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import java.util.List;

@Service
public class TopicService {
    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository,
                        UserRepository userRepository,
                        Topics_CommentRepository topicsCommentRepository) {
        this.topicRepository = topicRepository;
    }

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public List<Skill> getSkillsByTopic(String selectedTopic) {
        Topic topic = topicRepository.findByTopic(selectedTopic);
        if (topic != null) {
            return topic.getSkills();
        }
        return null;
    }




}
