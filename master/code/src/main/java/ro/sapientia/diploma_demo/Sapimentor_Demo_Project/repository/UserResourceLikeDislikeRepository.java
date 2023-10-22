package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Resources;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.UserResourceLikeDislike;

public interface UserResourceLikeDislikeRepository extends JpaRepository<UserResourceLikeDislike, Long> {
    UserResourceLikeDislike save(UserResourceLikeDislike userResourceLikeDislike);
    UserResourceLikeDislike findByUserAndResources(User userId, Resources resourceId);
}
