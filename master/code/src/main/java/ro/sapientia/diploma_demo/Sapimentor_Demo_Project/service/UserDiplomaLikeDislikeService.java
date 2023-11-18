package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.Diploma_TLikeDislike_DTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Diploma_Theses;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.UserDiploma_TLikeDislike;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.UserLikeAndDislikeData;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.DiplomaThesesRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserDiplomaLikeDislikeRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
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

        UserDiploma_TLikeDislike userDiploma_TLikeStatus = userDiplomaLikeDislikeRepository.findByUserAndDiplomaThesesId(userId, diplomaId)
                .orElse(null);

        if (userDiploma_TLikeStatus == null) {
            UserDiploma_TLikeDislike newactivelike = new UserDiploma_TLikeDislike();
            newactivelike.setUser(userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId)));
            newactivelike.setDiplomaTheses(diplomaThesesRepository.findById(diplomaId)
                            .orElseThrow(() -> new ResourceNotFoundException("Diploma not found with ID: " + diplomaId)));
            newactivelike.setLike(1);
            userDiplomaLikeDislikeRepository.save(newactivelike);
        } else {
            userDiploma_TLikeStatus.setLike(1);
            userDiplomaLikeDislikeRepository.updateLike(userDiploma_TLikeStatus);
        }
    }

    // TODO: Implement ChangeLikeStatusToInactive
    public void ChangeLikeStatusToInactive(Long diplomaId, Long userId) {
        UserDiploma_TLikeDislike userDiploma_TLikeStatus = userDiplomaLikeDislikeRepository.findByUserAndDiplomaThesesId(userId, diplomaId)
                .orElse(null);

        if (userDiploma_TLikeStatus == null) {
            UserDiploma_TLikeDislike newinactivelike = new UserDiploma_TLikeDislike();
            newinactivelike.setLike(0);
            userDiplomaLikeDislikeRepository.save(newinactivelike);
        } else {
            userDiploma_TLikeStatus.setLike(0);
            userDiplomaLikeDislikeRepository.updateLike(userDiploma_TLikeStatus);
        }
    }

    // TODO: Implement ChangeDislikeStatusToActive
    public void ChangeDislikeStatusToActive(Long diplomaId, Long userId) {
        UserDiploma_TLikeDislike userDiploma_TDislikeStatus = userDiplomaLikeDislikeRepository.findByUserAndDiplomaThesesId(userId, diplomaId)
                .orElse(null);

            if (userDiploma_TDislikeStatus == null) {
                UserDiploma_TLikeDislike newactivedislike = new UserDiploma_TLikeDislike();
                newactivedislike.setUser(userRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId)));
                newactivedislike.setDiplomaTheses(diplomaThesesRepository.findById(diplomaId)
                        .orElseThrow(() -> new ResourceNotFoundException("Diploma not found with ID: " + diplomaId)));
                newactivedislike.setDislike(1);
                userDiplomaLikeDislikeRepository.save(newactivedislike);
            } else {
                userDiploma_TDislikeStatus.setDislike(1);
                userDiplomaLikeDislikeRepository.updateDislike(userDiploma_TDislikeStatus);
            }
    }

    // TODO: Implement ChangeDislikeStatusToInactive
    public void ChangeDislikeStatusToInactive(Long diplomaId, Long userId) {
        UserDiploma_TLikeDislike userDiploma_TDislikeStatus = userDiplomaLikeDislikeRepository.findByUserAndDiplomaThesesId(userId, diplomaId)
                .orElse(null);

        if (userDiploma_TDislikeStatus == null) {
            UserDiploma_TLikeDislike newinactivedislike = new UserDiploma_TLikeDislike();
            newinactivedislike.setDislike(0);
            userDiplomaLikeDislikeRepository.save(newinactivedislike);
        } else {
            userDiploma_TDislikeStatus.setDislike(0);
            userDiplomaLikeDislikeRepository.updateDislike(userDiploma_TDislikeStatus);
        }
    }

    // TODO: Implement ChangeLikeStatusToActiveAndDislikeToInactive
    public void ChangeLikeStatusToActiveAndDislikeToInactive(Long diplomaId, Long userId) {
        UserDiploma_TLikeDislike userDiploma_TLikeAndDislikeStatus = userDiplomaLikeDislikeRepository.findByUserAndDiplomaThesesId(userId, diplomaId)
                .orElse(null);

        if (userDiploma_TLikeAndDislikeStatus == null) {
            UserDiploma_TLikeDislike newLikeAndDislike = new UserDiploma_TLikeDislike();
            newLikeAndDislike.setLike(1);
            newLikeAndDislike.setDislike(0);
            userDiplomaLikeDislikeRepository.save(newLikeAndDislike);
        } else {
            userDiploma_TLikeAndDislikeStatus.setLike(1);
            userDiploma_TLikeAndDislikeStatus.setDislike(0);
            userDiplomaLikeDislikeRepository.updateLikeAndDislike(userDiploma_TLikeAndDislikeStatus);
        }
    }

    // TODO: Implement ChangeDislikeStatusToActiveAndLikeToInactive
    public void ChangeDislikeStatusToActiveAndLikeToInactive(Long diplomaId, Long userId) {
        UserDiploma_TLikeDislike userDiploma_TLikeAndDislikeStatus = userDiplomaLikeDislikeRepository.findByUserAndDiplomaThesesId(userId, diplomaId)
                .orElse(null);

        if (userDiploma_TLikeAndDislikeStatus == null) {
            UserDiploma_TLikeDislike newLikeAndDislike = new UserDiploma_TLikeDislike();
            newLikeAndDislike.setLike(0);
            newLikeAndDislike.setDislike(1);
            userDiplomaLikeDislikeRepository.save(newLikeAndDislike);
        } else {
            userDiploma_TLikeAndDislikeStatus.setLike(0);
            userDiploma_TLikeAndDislikeStatus.setDislike(1);
            userDiplomaLikeDislikeRepository.updateLikeAndDislike(userDiploma_TLikeAndDislikeStatus);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
    @Cacheable(value = "getDiplomaLikeAndDislikeStatus")
    public List<UserLikeAndDislikeData> getLikeAndDislikeStatus(Long userId) {
        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        //System.out.println("user_Id: " + user_Id.getId());
        List<Diploma_TLikeDislike_DTO> userLikeAndDislikes = userDiplomaLikeDislikeRepository.findAllByUserId(user_Id);

        //System.out.println("userLikeAndDislikes: " + userLikeAndDislikes);
        List<UserLikeAndDislikeData> DiplomaThesesLikeAndDislikeDataList = new ArrayList<>();

        for (Diploma_TLikeDislike_DTO userLikeAndDislike : userLikeAndDislikes) {
            Long diplomaId = userLikeAndDislike.getDiploma_id();
            Long userId1 = userLikeAndDislike.getUser_id();
            int like = userLikeAndDislike.getLike();
            int dislike = userLikeAndDislike.getDislike();
            UserLikeAndDislikeData userlikeAndDislikeData = new UserLikeAndDislikeData(diplomaId, userId1, like, dislike);
            //System.out.println("Igen:" + userlikeAndDislikeData);
            DiplomaThesesLikeAndDislikeDataList.add(userlikeAndDislikeData);
        }
        //System.out.println("DiplomaThesesLikeAndDislikeDataList: " + DiplomaThesesLikeAndDislikeDataList);
        return DiplomaThesesLikeAndDislikeDataList;
    }
}
