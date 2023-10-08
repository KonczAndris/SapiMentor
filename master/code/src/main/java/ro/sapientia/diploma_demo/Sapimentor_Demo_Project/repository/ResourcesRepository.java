package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Resources;

@Repository
public interface ResourcesRepository extends JpaRepository<Resources, Long> {
    Resources save(Resources resources);
}
