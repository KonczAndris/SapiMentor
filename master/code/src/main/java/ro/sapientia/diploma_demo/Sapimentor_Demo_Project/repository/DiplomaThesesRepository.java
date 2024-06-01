package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.Diploma_TLikeDislike_DTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Diploma_Theses;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiplomaThesesRepository extends JpaRepository<Diploma_Theses, Long> {
    Diploma_Theses save(Diploma_Theses diploma_theses);

    @Modifying
    @Query("UPDATE Diploma_Theses dt SET dt.like = :#{#diploma_theses.like}, dt.dislike = :#{#diploma_theses.dislike} WHERE dt.id = :#{#diploma_theses.id}")
    void update(@Param("diploma_theses") Diploma_Theses diploma_theses);

    @Query("SELECT new ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.Diploma_TLikeDislike_DTO(dt.id, dt.like, dt.dislike)  FROM Diploma_Theses dt WHERE dt.id = :diplomaId")
    List<Diploma_TLikeDislike_DTO> findLikeDislikeById(Long diplomaId);

    @Query("SELECT d.id, d.name, d.year, d.topic_name,d.user_name,d.like,d.dislike,d.keywords, d.user_id FROM Diploma_Theses d")
    List<Object[]> findProjectedBy();

    @Query("SELECT new ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Diploma_Theses(d.id, d.like, d.dislike) FROM Diploma_Theses d WHERE d.id = :diplomaId")
    Optional<Diploma_Theses> findLikeAndDislikeById(Long diplomaId);

    @Query("SELECT d.id, d.diploma_theses_file FROM Diploma_Theses d WHERE d.id = :diplomaId")
    List<Object[]> findDiplomaPDFById(Long diplomaId);

<<<<<<< HEAD
//    @Query("SELECT d.id, d.abstract_file_en FROM Diploma_Theses d WHERE d.id = :diplomaId")
//    List<Object[]> findDiplomaEnAbstractPDFById(Long diplomaId);
//
//    @Query("SELECT d.id, d.abstract_file_hu FROM Diploma_Theses d WHERE d.id = :diplomaId")
//    List<Object[]> findDiplomaHunAbstractPDFById(Long diplomaId);

=======
    @Query("SELECT d.id, d.abstract_file_hu FROM Diploma_Theses d WHERE d.id = :diplomaId")
    List<Object[]> findHu_AbstractPDFById(Long diplomaId);

    @Query("SELECT d.id, d.abstract_file_en FROM Diploma_Theses d WHERE d.id = :diplomaId")
    List<Object[]> findEn_AbstractPDFById(Long diplomaId);
>>>>>>> dd9e4f490795e131009ef4fd4e0231d2421ef7b7

    //////////////////////// ez kell a tobbihez is /////////////////////////////////////////////////////////////////
    @Modifying
    @Query("DELETE FROM Diploma_Theses dt WHERE dt.id = :diplomaId")
    void deleteById(Long diplomaId);

    @Query("SELECT d.id, d.name, d.year, d.topic_name,d.user_name,d.like,d.dislike,d.keywords, d.user_id FROM Diploma_Theses d WHERE LOWER(d.name) LIKE %:name% AND d.topic_name IN :topicNames")
    List<Object[]> findAllByNameAndTopicName(String name, String[] topicNames);

    @Query("SELECT d.id, d.name, d.year, d.topic_name,d.user_name,d.like,d.dislike,d.keywords, d.user_id FROM Diploma_Theses d WHERE LOWER(d.keywords) LIKE %:keyword% AND d.topic_name IN :topicNames")
    List<Object[]> findAllByKeywordAndTopicName(String keyword, String[] topicNames);

    @Query("SELECT  d FROM Diploma_Theses d WHERE d.name = :name AND d.topic_name = :topicName AND d.year = :year")
    Diploma_Theses findNameAndTopic(String name, String topicName, String year);

}
