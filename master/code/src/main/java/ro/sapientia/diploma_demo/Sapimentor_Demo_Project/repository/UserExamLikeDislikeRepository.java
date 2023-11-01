package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Exams;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.UserExamLikeDislike;

import java.util.List;

public interface UserExamLikeDislikeRepository extends JpaRepository<UserExamLikeDislike, Long> {
    UserExamLikeDislike save(UserExamLikeDislike userExamLikeDislike);
    UserExamLikeDislike findByUserAndExams(User userId, Exams examId);
    List<UserExamLikeDislike> findAllByUser(User userId);
}
