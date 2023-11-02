package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Diploma_Theses;

@Repository
public interface DiplomaThesesRepository extends JpaRepository<Diploma_Theses, Long> {
    Diploma_Theses save(Diploma_Theses diploma_theses);
}
