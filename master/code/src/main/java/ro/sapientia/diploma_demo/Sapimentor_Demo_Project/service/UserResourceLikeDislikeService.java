package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.LinksLikeDislikeDTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Resources;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.UserLikeAndDislikeData;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.UserResourceLikeDislike;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ResourcesRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserResourceLikeDislikeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserResourceLikeDislikeService {


    private final UserResourceLikeDislikeRepository userResourceLikeDislikeRepository;
    private final ResourcesRepository resourcesRepository;
    private final UserRepository userRepository;

    public UserResourceLikeDislikeService(UserResourceLikeDislikeRepository userResourceLikeDislikeRepository,
                                          ResourcesRepository resourcesRepository,
                                          UserRepository userRepository) {
        this.userResourceLikeDislikeRepository = userResourceLikeDislikeRepository;
        this.resourcesRepository = resourcesRepository;
        this.userRepository = userRepository;
    }

    public void ChangeLikeStatusToActive(Long resourceId, Long userId) {
        Resources resource_Id = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        UserResourceLikeDislike existingactivelike = userResourceLikeDislikeRepository.findByUserAndResources(user_Id, resource_Id);

        if(existingactivelike == null){
            UserResourceLikeDislike newactivelike = new UserResourceLikeDislike();
            newactivelike.setUser(user_Id);
            newactivelike.setResources(resource_Id);
            newactivelike.setLike(1);
            userResourceLikeDislikeRepository.save(newactivelike);
        } else {
            existingactivelike.setLike(1);
            userResourceLikeDislikeRepository.save(existingactivelike);
        }
    }

    public void ChangeLikeStatusToInactive(Long resourceId, Long userId) {
        Resources resource_Id = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserResourceLikeDislike existinginactivelike = userResourceLikeDislikeRepository.findByUserAndResources(user_Id, resource_Id);

        if (existinginactivelike == null) {
            UserResourceLikeDislike newinactivelike = new UserResourceLikeDislike();
            newinactivelike.setUser(user_Id);
            newinactivelike.setResources(resource_Id);
            newinactivelike.setLike(0);
            userResourceLikeDislikeRepository.save(newinactivelike);
        } else {
            existinginactivelike.setLike(0);
            userResourceLikeDislikeRepository.save(existinginactivelike);
        }
    }

    public void ChangeDislikeStatusToActive(Long resourceId, Long userId) {
        Resources resource_Id = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserResourceLikeDislike existingactivedislike = userResourceLikeDislikeRepository.findByUserAndResources(user_Id, resource_Id);

        if(existingactivedislike == null){
            UserResourceLikeDislike newactivedislike = new UserResourceLikeDislike();
            newactivedislike.setUser(user_Id);
            newactivedislike.setResources(resource_Id);
            newactivedislike.setDislike(1);
            userResourceLikeDislikeRepository.save(newactivedislike);
        } else {
            existingactivedislike.setDislike(1);
            userResourceLikeDislikeRepository.save(existingactivedislike);
        }
    }

    public void ChangeDislikeStatusToInactive(Long resourceId, Long userId) {
        Resources resource_Id = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserResourceLikeDislike existinginactivedislike = userResourceLikeDislikeRepository.findByUserAndResources(user_Id, resource_Id);

        if (existinginactivedislike == null){
            UserResourceLikeDislike newinactivedislike = new UserResourceLikeDislike();
            newinactivedislike.setUser(user_Id);
            newinactivedislike.setResources(resource_Id);
            newinactivedislike.setDislike(0);
            userResourceLikeDislikeRepository.save(newinactivedislike);
        } else {
            existinginactivedislike.setDislike(0);
            userResourceLikeDislikeRepository.save(existinginactivedislike);
        }
    }

    public void ChangeLikeStatusToActiveAndDislikeToInactive(Long resourceId, Long userId){
        Resources resource_Id = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserResourceLikeDislike existingLikeAndDislike = userResourceLikeDislikeRepository.findByUserAndResources(user_Id, resource_Id);

        if (existingLikeAndDislike == null){
            UserResourceLikeDislike newLikeAndDislike = new UserResourceLikeDislike();
            newLikeAndDislike.setUser(user_Id);
            newLikeAndDislike.setResources(resource_Id);
            newLikeAndDislike.setLike(1);
            newLikeAndDislike.setDislike(0);
            userResourceLikeDislikeRepository.save(newLikeAndDislike);
        } else {
            existingLikeAndDislike.setLike(1);
            existingLikeAndDislike.setDislike(0);
            userResourceLikeDislikeRepository.save(existingLikeAndDislike);
        }
    }

    public void ChangeDislikeStatusToActiveAndLikeToInactive(Long resourceId, Long userId){
        Resources resource_Id = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserResourceLikeDislike existingLikeAndDislike = userResourceLikeDislikeRepository.findByUserAndResources(user_Id, resource_Id);

        if (existingLikeAndDislike == null){
            UserResourceLikeDislike newLikeAndDislike = new UserResourceLikeDislike();
            newLikeAndDislike.setUser(user_Id);
            newLikeAndDislike.setResources(resource_Id);
            newLikeAndDislike.setLike(0);
            newLikeAndDislike.setDislike(1);
            userResourceLikeDislikeRepository.save(newLikeAndDislike);
        } else {
            existingLikeAndDislike.setLike(0);
            existingLikeAndDislike.setDislike(1);
            userResourceLikeDislikeRepository.save(existingLikeAndDislike);
        }
    }

    @Cacheable(value = "likeStatus")
    public String getLikeStatus(Long resourceId, Long userId){
        Resources resource_Id = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserResourceLikeDislike existinglike = userResourceLikeDislikeRepository.findByUserAndResources(user_Id, resource_Id);

        if (existinglike != null){
            return existinglike.getLike().toString();
        } else {
            return "0";
        }
    }

    @Cacheable(value = "dislikeStatus")
    public String getDislikeStatus(Long resourceId, Long userId){
        Resources resource_Id = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserResourceLikeDislike existingdislike = userResourceLikeDislikeRepository.findByUserAndResources(user_Id, resource_Id);

        if (existingdislike != null){
            return existingdislike.getDislike().toString();
        } else {
            return "0";
        }
    }

    public List<UserLikeAndDislikeData> getLikeAndDislikeStatus(Long userId){
        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        List<LinksLikeDislikeDTO> userLikesAndDislikes = userResourceLikeDislikeRepository.findAllByUserId(user_Id);
        List<UserLikeAndDislikeData> ResourcesLikeAndDislikeDataList = new ArrayList<>();

        for (LinksLikeDislikeDTO userLikeAndDislike : userLikesAndDislikes) {
            Long resourceId = userLikeAndDislike.getResource_id();
            Long userId1 = userLikeAndDislike.getUser_id();
            int like = userLikeAndDislike.getLike();
            int dislike = userLikeAndDislike.getDislike();
            UserLikeAndDislikeData userLikeAndDislikeData = new UserLikeAndDislikeData(resourceId,userId1 , like, dislike);
            ResourcesLikeAndDislikeDataList.add(userLikeAndDislikeData);
        }
        return ResourcesLikeAndDislikeDataList;
    }
}
