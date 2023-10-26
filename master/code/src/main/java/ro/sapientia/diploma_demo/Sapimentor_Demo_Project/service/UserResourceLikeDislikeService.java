package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Resources;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.UserResourceLikeDislike;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ResourcesRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserResourceLikeDislikeRepository;

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

    // itt allitom at a like statuszt aktivra vagyis 1-re
    public void ChangeLikeStatusToActive(Long resourceId, Long userId) {
        // itt meg keresem a resource-t, hogy letezike-e
        Resources resource_Id = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        // itt meg keresem a user-t, hogy letezik-e
        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
//        System.out.println("resource_Id: " + resource_Id.getId());
//        System.out.println("user_Id: " + user_Id.getId());
        UserResourceLikeDislike existingactivelike = userResourceLikeDislikeRepository.findByUserAndResources(user_Id, resource_Id);
        //System.out.println("existingactivelike: " + existingactivelike);

        if(existingactivelike == null){
            UserResourceLikeDislike newactivelike = new UserResourceLikeDislike();
            newactivelike.setUser(user_Id);
            newactivelike.setResources(resource_Id);
            newactivelike.setLike(1);
            //newactivelike.setDislike(0);
            userResourceLikeDislikeRepository.save(newactivelike);
        } else {
            existingactivelike.setLike(1);
            //existingactivelike.setDislike(0);
            userResourceLikeDislikeRepository.save(existingactivelike);
        }
    }

    // itt allitom at a like statuszt inaktivra vagyis 0-ra
    public void ChangeLikeStatusToInactive(Long resourceId, Long userId) {
        // itt meg keresem a resource-t, hogy letezike-e
        Resources resource_Id = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        // itt meg keresem a user-t, hogy letezik-e
        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserResourceLikeDislike existinginactivelike = userResourceLikeDislikeRepository.findByUserAndResources(user_Id, resource_Id);

        if (existinginactivelike == null) {
            UserResourceLikeDislike newinactivelike = new UserResourceLikeDislike();
            newinactivelike.setUser(user_Id);
            newinactivelike.setResources(resource_Id);
            newinactivelike.setLike(0);
            //newinactivelike.setDislike(0);
            userResourceLikeDislikeRepository.save(newinactivelike);
        } else {
            existinginactivelike.setLike(0);
            //existinginactivelike.setDislike(0);
            userResourceLikeDislikeRepository.save(existinginactivelike);
        }
    }


    // itt allitom at a dislike statuszt aktivra vagyis 1-re
    public void ChangeDislikeStatusToActive(Long resourceId, Long userId) {
        // itt meg keresem a resource-t, hogy letezike-e
        Resources resource_Id = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        // itt meg keresem a user-t, hogy letezik-e
        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserResourceLikeDislike existingactivedislike = userResourceLikeDislikeRepository.findByUserAndResources(user_Id, resource_Id);

        if(existingactivedislike == null){
            UserResourceLikeDislike newactivedislike = new UserResourceLikeDislike();
            newactivedislike.setUser(user_Id);
            newactivedislike.setResources(resource_Id);
            //newactivedislike.setLike(0);
            newactivedislike.setDislike(1);
            userResourceLikeDislikeRepository.save(newactivedislike);
        } else {
            //existingactivedislike.setLike(0);
            existingactivedislike.setDislike(1);
            userResourceLikeDislikeRepository.save(existingactivedislike);
        }
    }

    //itt allitom at a dislike statuszt inaktivra vagyis 0-ra
    public void ChangeDislikeStatusToInactive(Long resourceId, Long userId) {
        // itt meg keresem a resource-t, hogy letezike-e
        Resources resource_Id = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        // itt meg keresem a user-t, hogy letezik-e
        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserResourceLikeDislike existinginactivedislike = userResourceLikeDislikeRepository.findByUserAndResources(user_Id, resource_Id);

        if (existinginactivedislike == null){
            UserResourceLikeDislike newinactivedislike = new UserResourceLikeDislike();
            newinactivedislike.setUser(user_Id);
            newinactivedislike.setResources(resource_Id);
            //newinactivedislike.setLike(0);
            newinactivedislike.setDislike(0);
            userResourceLikeDislikeRepository.save(newinactivedislike);
        } else {
            //existinginactivedislike.setLike(0);
            existinginactivedislike.setDislike(0);
            userResourceLikeDislikeRepository.save(existinginactivedislike);
        }
    }

    public void ChangeLikeStatusToActiveAndDislikeToInactive(Long resourceId, Long userId){
        // itt meg keresem a resource-t, hogy letezike-e
        Resources resource_Id = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        // itt meg keresem a user-t, hogy letezik-e
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
        // itt meg keresem a resource-t, hogy letezike-e
        Resources resource_Id = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        // itt meg keresem a user-t, hogy letezik-e
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

    // itt kerem le a like statuszt
    //@Cacheable(value = "likeStatus")
    public String getLikeStatus(Long resourceId, Long userId){
        // itt meg keresem a resource-t, hogy letezike-e
        Resources resource_Id = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        // itt meg keresem a user-t, hogy letezik-e
        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserResourceLikeDislike existinglike = userResourceLikeDislikeRepository.findByUserAndResources(user_Id, resource_Id);
        //System.out.println("existinglike: " + existinglike.getLike());
        if (existinglike != null){
            return existinglike.getLike().toString();
        } else {
            return "0";
        }
    }

    // itt kerem le a dislike statuszt
    //@Cacheable(value = "dislikeStatus")
    public String getDislikeStatus(Long resourceId, Long userId){
        // itt meg keresem a resource-t, hogy letezike-e
        Resources resource_Id = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));
        // itt meg keresem a user-t, hogy letezik-e
        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserResourceLikeDislike existingdislike = userResourceLikeDislikeRepository.findByUserAndResources(user_Id, resource_Id);
        //System.out.println("existinglike: " + existinglike.getLike());
        if (existingdislike != null){
            return existingdislike.getDislike().toString();
        } else {
            return "0";
        }
    }
}
