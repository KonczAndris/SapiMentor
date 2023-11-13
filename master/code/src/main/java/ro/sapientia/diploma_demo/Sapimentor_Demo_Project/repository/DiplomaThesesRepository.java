package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Diploma_Theses;

import java.util.List;

@Repository
public interface DiplomaThesesRepository extends JpaRepository<Diploma_Theses, Long> {
    Diploma_Theses save(Diploma_Theses diploma_theses);

    @Query("SELECT d.id, d.name, d.year, d.topic_name,d.user_name,d.like,d.dislike FROM Diploma_Theses d")
    List<Object[]> findProjectedBy();

    @Query("SELECT d.diploma_theses_file, d.id FROM Diploma_Theses d")
    List<Object[]> findAllDiplomaPDFById();
}
