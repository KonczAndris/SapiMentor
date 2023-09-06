package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Profile_Topics;

import java.util.Optional;

@Repository
public interface ProfileTopicsRepository extends JpaRepository<Profile_Topics, Long> {
    Profile_Topics save(Profile_Topics profile_topics);
}



//void save(Profile_Topics profile_topics);
//Optional<Profile_Topics> findById(Long id);
