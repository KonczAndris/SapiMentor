package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.RatingDTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Rating;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Rating save(Rating rating);

    @Cacheable("findByUserIdAndRatedUserId")
    Rating findByUserIdAndRatedUserId(Long userId, Long ratedUserId);

    @Query("SELECT new ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.RatingDTO(r.score) FROM Rating r WHERE r.ratedUserId = :ratedUserId")
    List<RatingDTO> findByRatedUserId(Long ratedUserId);

    @Modifying
    @Query("DELETE FROM Rating r WHERE r.userId = :ratingUserId AND r.ratedUserId = :ratedUserId")
    void deleteByUserIdAndRatedUserId(Long ratingUserId, Long ratedUserId);

    @Query("SELECT r FROM Rating r WHERE r.ratedUserId != :userId")
    List<Rating> findAllOther(@Param("userId") Long userId);

    // olyan ratingokat keres, ahol a user role-ja MENTEE
    @Query("SELECT r FROM Rating r JOIN User u ON r.ratedUserId = u.id JOIN u.roles ur ON ur.name = 'MENTEE' WHERE r.userId = :userId")
    List<Rating> findAllMenteeRatingById(@Param("userId") Long userId);

    @Query("SELECT r FROM Rating r JOIN User u ON r.ratedUserId = u.id JOIN u.roles ur ON ur.name = 'MENTOR' WHERE r.userId = :userId")
    List<Rating> findAllMentorRatingById(@Param("userId") Long userId);

//    @Query("SELECT r FROM Rating r WHERE r.ratedUserId = :userId")
//    List<Rating> findAllByUserId(Long userId);
    @Query("SELECT r FROM Rating r WHERE r.ratedUserId = :userId")
    List<Rating> findAllByRatedUserId(Long userId);

    @Modifying
    @Query("DELETE FROM Rating r WHERE r.userId = :userId OR r.ratedUserId = :userId")
    void deleteAllByUserId(Long userId);

}
