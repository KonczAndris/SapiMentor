package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.Diploma_TLikeDislike_DTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Diploma_Theses;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.UserDiploma_TLikeDislike;

import java.util.List;
import java.util.Optional;


public interface UserDiplomaLikeDislikeRepository extends JpaRepository<UserDiploma_TLikeDislike, Long> {
    // TODO: Implement save
    UserDiploma_TLikeDislike save(UserDiploma_TLikeDislike userDiploma_tLikeDislike);

    @Modifying
    @Query("UPDATE UserDiploma_TLikeDislike dt SET dt.like = :#{#userDiploma_TLikeStatus.like} WHERE dt.id = :#{#userDiploma_TLikeStatus.id}")
    void updateLike(@Param("userDiploma_TLikeStatus") UserDiploma_TLikeDislike userDiploma_TLikeStatus);

    @Modifying
    @Query("UPDATE UserDiploma_TLikeDislike dt SET dt.dislike = :#{#userDiploma_TDislikeStatus.dislike} WHERE dt.id = :#{#userDiploma_TDislikeStatus.id}")
    void updateDislike(@Param("userDiploma_TDislikeStatus") UserDiploma_TLikeDislike userDiploma_TDislikeStatus);

    @Modifying
    @Query("UPDATE UserDiploma_TLikeDislike dt SET dt.like = :#{#userDiploma_TLikeAndDislikeStatus.like}, dt.dislike = :#{#userDiploma_TLikeAndDislikeStatus.dislike} WHERE dt.id = :#{#userDiploma_TLikeAndDislikeStatus.id}")
    void updateLikeAndDislike(@Param("userDiploma_TLikeAndDislikeStatus") UserDiploma_TLikeDislike userDiploma_TLikeAndDislikeStatus);

    // TODO: Implement findByUserAndDiplomaTheses
    UserDiploma_TLikeDislike findByUserAndDiplomaTheses(User userId, Diploma_Theses diplomaId);

    @Query("SELECT new ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.UserDiploma_TLikeDislike(dt.id, dt.like, dt.dislike) FROM UserDiploma_TLikeDislike dt WHERE dt.user.id = :userId AND dt.diplomaTheses.id = :diplomaId")
    Optional<UserDiploma_TLikeDislike> findByUserAndDiplomaThesesId(Long userId, Long diplomaId);

    // TODO: Implement findAllByUser
    //List<UserDiploma_TLikeDislike> findAllByUser(User userId);

//    @Query("SELECT dt.id , dt.user.id , dt.diplomaTheses.id, dt.like, dt.dislike  FROM UserDiploma_TLikeDislike dt WHERE dt.user = :userId")
//    List<Diploma_T_DTO> findAllByUserId(User userId);

    @Query("SELECT new ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.Diploma_TLikeDislike_DTO(dt.id, dt.user.id, dt.diplomaTheses.id, dt.like, dt.dislike) FROM UserDiploma_TLikeDislike dt WHERE dt.user = :userId")
    List<Diploma_TLikeDislike_DTO> findAllByUserId(User userId);

    @Modifying
    @Query("DELETE FROM UserDiploma_TLikeDislike udtld WHERE udtld.user.id = :userId")
    void deleteByUserId(Long userId);

    //////////////////////// ez kell a tobbihez is /////////////////////////////////////////////////////////////////
    @Modifying
    @Query("DELETE FROM UserDiploma_TLikeDislike udtld WHERE udtld.diplomaTheses.id = :diplomaId")
    void deleteByDiplomaId(Long diplomaId);


}
