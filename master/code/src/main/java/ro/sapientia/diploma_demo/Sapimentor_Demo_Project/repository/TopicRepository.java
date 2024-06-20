package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Topic;


public interface TopicRepository extends JpaRepository<Topic, Long> {
    Topic findByTopic(String topic);

}
