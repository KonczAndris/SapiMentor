package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;

//megkerdezni, hogy ez miert kell
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
