package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Diploma_Theses;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.UserDiploma_TLikeDislike;

import java.util.List;


public interface UserDiplomaLikeDislikeRepository extends JpaRepository<UserDiploma_TLikeDislike, Long> {
    // TODO: Implement save
    UserDiploma_TLikeDislike save(UserDiploma_TLikeDislike userDiploma_tLikeDislike);

    // TODO: Implement findByUserAndDiplomaTheses
    UserDiploma_TLikeDislike findByUserAndDiplomaTheses(User userId, Diploma_Theses diplomaId);

    // TODO: Implement findAllByUser
    List<UserDiploma_TLikeDislike> findAllByUser(User userId);

}
