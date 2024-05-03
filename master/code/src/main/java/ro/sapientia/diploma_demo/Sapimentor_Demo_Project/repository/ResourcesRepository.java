package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Resources;

import java.util.List;

@Repository
public interface ResourcesRepository extends JpaRepository<Resources, Long> {
    Resources save(Resources resources);

    @Modifying
    @Query("DELETE FROM Resources r WHERE r.id = :resourceId")
    void deleteById(Long resourceId);

    @Query("SELECT r FROM Resources r WHERE LOWER(r.name) LIKE %:name% AND r.topic_name IN :topicNames")
    List<Resources> findAllByNameAndTopicName(String name, String[] topicNames);
}
