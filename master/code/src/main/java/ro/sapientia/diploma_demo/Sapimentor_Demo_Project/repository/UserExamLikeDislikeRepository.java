package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.ExamsLikeDislikeDTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Exams;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.UserExamLikeDislike;

import java.util.List;
import java.util.Optional;

public interface UserExamLikeDislikeRepository extends JpaRepository<UserExamLikeDislike, Long> {
    UserExamLikeDislike save(UserExamLikeDislike userExamLikeDislike);

    @Modifying
    @Query("UPDATE UserExamLikeDislike ueld SET ueld.like = :#{#userExamLikeStatus.like} WHERE ueld.id = :#{#userExamLikeStatus.id}")
    void updateLike(@Param("userExamLikeStatus") UserExamLikeDislike userExamLikeStatus);

    @Modifying
    @Query("UPDATE UserExamLikeDislike ueld SET ueld.dislike = :#{#userExamDislikeStatus.dislike} WHERE ueld.id = :#{#userExamDislikeStatus.id}")
    void updateDislike(@Param("userExamDislikeStatus") UserExamLikeDislike userExamDislikeStatus);

    @Modifying
    @Query("UPDATE UserExamLikeDislike ueld SET ueld.like = :#{#userExamLikeAndDislikeStatus.like}, ueld.dislike = :#{#userExamLikeAndDislikeStatus.dislike} WHERE ueld.id = :#{#userExamLikeAndDislikeStatus.id}")
    void updateLikeAndDislike(@Param("userExamLikeAndDislikeStatus") UserExamLikeDislike userExamLikeAndDislikeStatus);

    UserExamLikeDislike findByUserAndExams(User userId, Exams examId);
//    List<UserExamLikeDislike> findAllByUser(User userId);
    @Query("SELECT new ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.ExamsLikeDislikeDTO(e.id, e.user.id, e.exams.id, e.like, e.dislike) FROM UserExamLikeDislike e WHERE e.user = :userId")
    List<ExamsLikeDislikeDTO> findAllByUserId(User userId);

    @Query("SELECT new ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.UserExamLikeDislike(ueld.id, ueld.like, ueld.dislike) FROM UserExamLikeDislike ueld WHERE ueld.user.id = :userId AND ueld.exams.id = :examId")
    Optional<UserExamLikeDislike> findByUserAndExamsId(Long userId, Long examId);
}
