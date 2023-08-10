package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;

import javax.transaction.Transactional;

//megkerdezni, hogy ez miert kell
<<<<<<< HEAD

@Transactional
=======
>>>>>>> parent of a297d3b (Update)
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    static int enableAppUser(String email) {
        return 0;
    }
}
