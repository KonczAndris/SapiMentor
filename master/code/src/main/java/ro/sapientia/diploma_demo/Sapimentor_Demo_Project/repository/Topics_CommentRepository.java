package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Topics_Comment;

import java.util.List;

@Repository
public interface Topics_CommentRepository extends JpaRepository<Topics_Comment, Long> {

    Topics_Comment save(Topics_Comment topicsComment);

    @Cacheable("findByUserIdAndRatedTopicId")
    Topics_Comment findByUserIdAndRatedTopicId(Long userId, String ratedTopicId);

    @Query("SELECT tc FROM Topics_Comment tc WHERE tc.ratedTopicId = :ratedTopicId")
    List<Topics_Comment> findAllByRatedTopicId(String ratedTopicId);

    @Query("SELECT tc FROM Topics_Comment tc WHERE tc.ratedTopicId = :topicId")
    List<Topics_Comment> findAllByRatedUserId(String topicId);
}
