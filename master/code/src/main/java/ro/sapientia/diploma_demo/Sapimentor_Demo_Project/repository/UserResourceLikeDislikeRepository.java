package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.LinksLikeDislikeDTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Resources;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.UserResourceLikeDislike;

import java.util.List;

public interface UserResourceLikeDislikeRepository extends JpaRepository<UserResourceLikeDislike, Long> {
    UserResourceLikeDislike save(UserResourceLikeDislike userResourceLikeDislike);
    UserResourceLikeDislike findByUserAndResources(User userId, Resources resourceId);
    @Query("SELECT new ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.LinksLikeDislikeDTO(urld.id, urld.user.id, urld.resources.id, urld.like, urld.dislike) FROM UserResourceLikeDislike urld WHERE urld.user = :userId")
    List<LinksLikeDislikeDTO> findAllByUserId(User userId);

    @Modifying
    @Query("DELETE FROM UserResourceLikeDislike urld WHERE urld.user.id = :userId")
    void deleteByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM UserResourceLikeDislike urld WHERE urld.resources.id = :resourceId")
    void deleteByResourceId(Long resourceId);
}
