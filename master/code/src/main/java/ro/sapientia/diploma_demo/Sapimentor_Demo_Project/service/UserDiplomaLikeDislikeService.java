package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Diploma_Theses;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.UserDiploma_TLikeDislike;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.UserLikeAndDislikeData;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.DiplomaThesesRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserDiplomaLikeDislikeRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDiplomaLikeDislikeService {
    private final UserDiplomaLikeDislikeRepository userDiplomaLikeDislikeRepository;
    private final DiplomaThesesRepository diplomaThesesRepository;
    private final UserRepository userRepository;

    public UserDiplomaLikeDislikeService(UserDiplomaLikeDislikeRepository userDiplomaLikeDislikeRepository,
                                         DiplomaThesesRepository diplomaThesesRepository,
                                         UserRepository userRepository) {
        this.userDiplomaLikeDislikeRepository = userDiplomaLikeDislikeRepository;
        this.diplomaThesesRepository = diplomaThesesRepository;
        this.userRepository = userRepository;
    }


    // TODO: Implement ChangeLikeStatusToActive
    public void ChangeLikeStatusToActive(Long diplomaId, Long userId) {
        Diploma_Theses diploma_Id = diplomaThesesRepository.findById(diplomaId)
                .orElseThrow(() -> new ResourceNotFoundException("Diploma not found with ID: " + diplomaId));

        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserDiploma_TLikeDislike existingactivelike = userDiplomaLikeDislikeRepository.findByUserAndDiplomaTheses(user_Id, diploma_Id);

        if (existingactivelike == null) {
            UserDiploma_TLikeDislike newactivelike = new UserDiploma_TLikeDislike();
            newactivelike.setUser(user_Id);
            newactivelike.setDiplomaTheses(diploma_Id);
            newactivelike.setLike(1);
            userDiplomaLikeDislikeRepository.save(newactivelike);
        } else {
            existingactivelike.setLike(1);
            userDiplomaLikeDislikeRepository.save(existingactivelike);
        }
    }

    // TODO: Implement ChangeLikeStatusToInactive
    public void ChangeLikeStatusToInactive(Long diplomaId, Long userId) {
        Diploma_Theses diploma_Id = diplomaThesesRepository.findById(diplomaId)
                .orElseThrow(() -> new ResourceNotFoundException("Diploma not found with ID: " + diplomaId));

        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserDiploma_TLikeDislike existinginactivelike = userDiplomaLikeDislikeRepository.findByUserAndDiplomaTheses(user_Id, diploma_Id);

        if (existinginactivelike == null) {
            UserDiploma_TLikeDislike newinactivelike = new UserDiploma_TLikeDislike();
            newinactivelike.setUser(user_Id);
            newinactivelike.setDiplomaTheses(diploma_Id);
            newinactivelike.setLike(0);
            userDiplomaLikeDislikeRepository.save(newinactivelike);
        } else {
            existinginactivelike.setLike(0);
            userDiplomaLikeDislikeRepository.save(existinginactivelike);
        }
    }

    // TODO: Implement ChangeDislikeStatusToActive
    public void ChangeDislikeStatusToActive(Long diplomaId, Long userId) {
        Diploma_Theses diploma_Id = diplomaThesesRepository.findById(diplomaId)
                .orElseThrow(() -> new ResourceNotFoundException("Diploma not found with ID: " + diplomaId));

        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

            UserDiploma_TLikeDislike existingactivedislike = userDiplomaLikeDislikeRepository.findByUserAndDiplomaTheses(user_Id, diploma_Id);

            if (existingactivedislike == null) {
                UserDiploma_TLikeDislike newactivedislike = new UserDiploma_TLikeDislike();
                newactivedislike.setUser(user_Id);
                newactivedislike.setDiplomaTheses(diploma_Id);
                newactivedislike.setDislike(1);
                userDiplomaLikeDislikeRepository.save(newactivedislike);
            } else {
                existingactivedislike.setDislike(1);
                userDiplomaLikeDislikeRepository.save(existingactivedislike);
            }
    }

    // TODO: Implement ChangeDislikeStatusToInactive
    public void ChangeDislikeStatusToInactive(Long diplomaId, Long userId) {
        Diploma_Theses diploma_Id = diplomaThesesRepository.findById(diplomaId)
                .orElseThrow(() -> new ResourceNotFoundException("Diploma not found with ID: " + diplomaId));

        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserDiploma_TLikeDislike existinginactivedislike = userDiplomaLikeDislikeRepository.findByUserAndDiplomaTheses(user_Id, diploma_Id);

        if (existinginactivedislike == null) {
            UserDiploma_TLikeDislike newinactivedislike = new UserDiploma_TLikeDislike();
            newinactivedislike.setUser(user_Id);
            newinactivedislike.setDiplomaTheses(diploma_Id);
            newinactivedislike.setDislike(0);
            userDiplomaLikeDislikeRepository.save(newinactivedislike);
        } else {
            existinginactivedislike.setDislike(0);
            userDiplomaLikeDislikeRepository.save(existinginactivedislike);
        }
    }

    // TODO: Implement ChangeLikeStatusToActiveAndDislikeToInactive
    public void ChangeLikeStatusToActiveAndDislikeToInactive(Long diplomaId, Long userId) {
        Diploma_Theses diploma_Id = diplomaThesesRepository.findById(diplomaId)
                .orElseThrow(() -> new ResourceNotFoundException("Diploma not found with ID: " + diplomaId));

        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserDiploma_TLikeDislike existingLikeAndDislike = userDiplomaLikeDislikeRepository.findByUserAndDiplomaTheses(user_Id, diploma_Id);

        if (existingLikeAndDislike == null) {
            UserDiploma_TLikeDislike newLikeAndDislike = new UserDiploma_TLikeDislike();
            newLikeAndDislike.setUser(user_Id);
            newLikeAndDislike.setDiplomaTheses(diploma_Id);
            newLikeAndDislike.setLike(1);
            newLikeAndDislike.setDislike(0);
            userDiplomaLikeDislikeRepository.save(newLikeAndDislike);
        } else {
            existingLikeAndDislike.setLike(1);
            existingLikeAndDislike.setDislike(0);
            userDiplomaLikeDislikeRepository.save(existingLikeAndDislike);
        }
    }

    // TODO: Implement ChangeDislikeStatusToActiveAndLikeToInactive
    public void ChangeDislikeStatusToActiveAndLikeToInactive(Long diplomaId, Long userId) {
        Diploma_Theses diploma_Id = diplomaThesesRepository.findById(diplomaId)
                .orElseThrow(() -> new ResourceNotFoundException("Diploma not found with ID: " + diplomaId));

        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserDiploma_TLikeDislike existingLikeAndDislike = userDiplomaLikeDislikeRepository.findByUserAndDiplomaTheses(user_Id, diploma_Id);

        if (existingLikeAndDislike == null) {
            UserDiploma_TLikeDislike newLikeAndDislike = new UserDiploma_TLikeDislike();
            newLikeAndDislike.setUser(user_Id);
            newLikeAndDislike.setDiplomaTheses(diploma_Id);
            newLikeAndDislike.setLike(0);
            newLikeAndDislike.setDislike(1);
            userDiplomaLikeDislikeRepository.save(newLikeAndDislike);
        } else {
            existingLikeAndDislike.setLike(0);
            existingLikeAndDislike.setDislike(1);
            userDiplomaLikeDislikeRepository.save(existingLikeAndDislike);
        }
    }

    // TODO: Implement getLikeStatus
    @Cacheable(value = "likeStatus")
    public String getLikeStatus(Long diplomaId, Long userId) {
        Diploma_Theses diploma_Id = diplomaThesesRepository.findById(diplomaId)
                .orElseThrow(() -> new ResourceNotFoundException("Diploma not found with ID: " + diplomaId));

        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

            UserDiploma_TLikeDislike existinglike = userDiplomaLikeDislikeRepository.findByUserAndDiplomaTheses(user_Id, diploma_Id);

            if (existinglike != null) {
                return existinglike.getLike().toString();
            } else {
                return "0";
            }
    }

    // TODO: Implement getDislikeStatus
    @Cacheable(value = "dislikeStatus")
    public String getDislikeStatus(Long diplomaId, Long userId) {
        Diploma_Theses diploma_Id = diplomaThesesRepository.findById(diplomaId)
                .orElseThrow(() -> new ResourceNotFoundException("Diploma not found with ID: " + diplomaId));

        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

            UserDiploma_TLikeDislike existingdislike = userDiplomaLikeDislikeRepository.findByUserAndDiplomaTheses(user_Id, diploma_Id);

            if (existingdislike != null) {
                return existingdislike.getDislike().toString();
            } else {
                return "0";
            }
    }

    // TODO: Implement getLikeAndDislikeStatus
    public List<UserLikeAndDislikeData> getLikeAndDislikeStatus(Long userId) {
        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        List<UserDiploma_TLikeDislike> userLikeAndDislikes = userDiplomaLikeDislikeRepository.findAllByUser(user_Id);

        List<UserLikeAndDislikeData> DiplomaThesesLikeAndDislikeDataList = new ArrayList<>();

        for (UserDiploma_TLikeDislike userLikeAndDislike : userLikeAndDislikes) {
            Long diplomaId = userLikeAndDislike.getDiplomaTheses().getId();
            Long userId1 = userLikeAndDislike.getUser().getId();
            int like = userLikeAndDislike.getLike();
            int dislike = userLikeAndDislike.getDislike();
            UserLikeAndDislikeData userlikeAndDislikeData = new UserLikeAndDislikeData(diplomaId, userId1, like, dislike);
            DiplomaThesesLikeAndDislikeDataList.add(userlikeAndDislikeData);
        }
        return DiplomaThesesLikeAndDislikeDataList;
    }
}
