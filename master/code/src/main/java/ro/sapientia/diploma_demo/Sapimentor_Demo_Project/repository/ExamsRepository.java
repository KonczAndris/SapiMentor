package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Exams;

import java.util.List;

@Repository
public interface ExamsRepository extends JpaRepository<Exams, Long> {
    Exams save(Exams exams);
    @Query("SELECT e.id, e.name, e.topic_name,e.user_name,e.like,e.dislike FROM Exams e")
    List<Object[]> findProjectedBy();

    @Query("SELECT e.examImage FROM Exams e WHERE e.id = :examId")
    byte[] findExamImageById(@Param("examId") Long examId);

}
