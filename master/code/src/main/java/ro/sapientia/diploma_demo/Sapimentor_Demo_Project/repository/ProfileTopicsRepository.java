package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Profile_Topics;

import java.util.List;

@Repository
public interface ProfileTopicsRepository extends JpaRepository<Profile_Topics, Long> {
    Profile_Topics save(Profile_Topics profile_topics);

    //findbyuserid szerint keresni
    List<Profile_Topics> findByUserId(Long id);

    @Query("SELECT pt.id FROM Profile_Topics pt WHERE pt.user.id = :userId")
    List<Object[]> findPTIdByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM Profile_Topics pt WHERE pt.user.id = :userId")
    void deleteByUserId(Long userId);



}
