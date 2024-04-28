package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.ExamsLikeDislikeDTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Exams;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamsRepository extends JpaRepository<Exams, Long> {
    Exams save(Exams exams);
    @Query("SELECT e.id, e.name, e.topic_name,e.user_name,e.like,e.dislike FROM Exams e")
    List<Object[]> findProjectedBy();

//    @Query("SELECT e.id, e.examImage FROM Exams e WHERE e.id = :examId")
//    List<Object[]> findExamImageById(Long examId);

    @Query("SELECT e.examImage, e.id FROM Exams e")
    List<Object[]> findAllExamImageById();

    @Query("SELECT ex.id, ex.examPDF FROM Exams ex WHERE ex.id = :examId")
    List<Object[]> findExamPDFById (Long examId);

    @Query("SELECT new ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.ExamsLikeDislikeDTO(ex.id, ex.like, ex.dislike) FROM Exams ex WHERE ex.id = :examId")
    List<ExamsLikeDislikeDTO> findLikeDislikeById(Long examId);

    @Query("SELECT new ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Exams(ex.id, ex.like, ex.dislike) FROM Exams ex WHERE ex.id = :examId")
    Optional<Exams> findLikeAndDislikeById(Long examId);

    @Modifying
    @Query("UPDATE Exams ex SET ex.like = :#{#exam.like}, ex.dislike = :#{#exam.dislike} WHERE ex.id = :#{#exam.id}")
    void update(@Param("exam") Exams exam);

}
