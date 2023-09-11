package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Profile_Topics;

import java.util.List;

@Repository
public interface ProfileTopicsRepository extends JpaRepository<Profile_Topics, Long> {
    Profile_Topics save(Profile_Topics profile_topics);

    //findbyuserid szerint keresni
    List<Profile_Topics> findByUserId(Long id);



}
