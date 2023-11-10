package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Exams;
@Repository
public interface ExamsRepository extends JpaRepository<Exams, Long> {
    Exams save(Exams exams);
}
