package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.ExamsLikeDislikeDTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Exams;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.UserExamLikeDislike;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.UserLikeAndDislikeData;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ExamsRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserExamLikeDislikeRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserExamLikeDislikeService {
    private final UserExamLikeDislikeRepository userExamLikeDislikeRepository;
    private final ExamsRepository examsRepository;
    private final UserRepository userRepository;

    public UserExamLikeDislikeService(UserExamLikeDislikeRepository userExamLikeDislikeRepository,
                                      ExamsRepository examsRepository,
                                      UserRepository userRepository) {
        this.userExamLikeDislikeRepository = userExamLikeDislikeRepository;
        this.examsRepository = examsRepository;
        this.userRepository = userRepository;
    }

    // TODO: beallitani a like statuszat aktivra
    // itt allitom at a like statuszt aktivra vagyis 1-re
    public void ChangeLikeStatusToActive(Long examId, Long userId) {

        UserExamLikeDislike userExamLikeStatus = userExamLikeDislikeRepository.findByUserAndExamsId(userId, examId)
                .orElse(null);

        if (userExamLikeStatus == null){
            UserExamLikeDislike newactivelike = new UserExamLikeDislike();
            newactivelike.setUser(userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId)));
            newactivelike.setExams(examsRepository.findById(examId)
                    .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId)));
            newactivelike.setLike(1);
            userExamLikeDislikeRepository.save(newactivelike);
        } else {
            userExamLikeStatus.setLike(1);
            userExamLikeDislikeRepository.updateLike(userExamLikeStatus);
        }
    }

    // TODO: beallitani a like statuszat inaktivra
    // itt allitom at a like statuszt inaktivra vagyis 0-ra
    public void ChangeLikeStatusToInactive(Long examId, Long userId) {
        UserExamLikeDislike userExamLikeStatus = userExamLikeDislikeRepository.findByUserAndExamsId(userId, examId)
                .orElse(null);


        if (userExamLikeStatus == null){
            UserExamLikeDislike newinactivelike = new UserExamLikeDislike();
            newinactivelike.setLike(0);
            userExamLikeDislikeRepository.save(newinactivelike);
        } else {
            userExamLikeStatus.setLike(0);
            userExamLikeDislikeRepository.updateLike(userExamLikeStatus);
        }
    }

    // TODO: beallitani a dislike statuszat aktivra
    // itt allitom at a dislike statuszt aktivra vagyis 1-re
    public void ChangeDislikeStatusToActive(Long examId, Long userId) {
        UserExamLikeDislike userExamDislikeStatus = userExamLikeDislikeRepository.findByUserAndExamsId(userId, examId)
                .orElse(null);

        if (userExamDislikeStatus == null){
            UserExamLikeDislike newactivedislike = new UserExamLikeDislike();
            newactivedislike.setUser(userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId)));
            newactivedislike.setExams(examsRepository.findById(examId)
                    .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId)));
            newactivedislike.setDislike(1);
            userExamLikeDislikeRepository.save(newactivedislike);
        } else {
            userExamDislikeStatus.setDislike(1);
            userExamLikeDislikeRepository.updateDislike(userExamDislikeStatus);
        }
    }

    // TODO: beallitani a dislike statuszat inaktivra
    //itt allitom at a dislike statuszt inaktivra vagyis 0-ra
    public void ChangeDislikeStatusToInactive(Long examId, Long userId) {
        UserExamLikeDislike userExamDislikeStatus = userExamLikeDislikeRepository.findByUserAndExamsId(userId, examId)
                .orElse(null);

        if (userExamDislikeStatus == null ){
            UserExamLikeDislike newinactivedislike = new UserExamLikeDislike();
            newinactivedislike.setDislike(0);
            userExamLikeDislikeRepository.save(newinactivedislike);
        } else {
            userExamDislikeStatus.setDislike(0);
            userExamLikeDislikeRepository.updateDislike(userExamDislikeStatus);
        }
    }

    // TODO: beallitani a like statuszat aktivra es a dislike statuszat inaktivra
    public void ChangeLikeStatusToActiveAndDislikeToInactive(Long examId, Long userId) {
        UserExamLikeDislike userExamLikeAndDislikeStatus = userExamLikeDislikeRepository.findByUserAndExamsId(userId, examId)
                .orElse(null);

        if (userExamLikeAndDislikeStatus == null){
            UserExamLikeDislike newLikeAndDislike = new UserExamLikeDislike();
            newLikeAndDislike.setLike(1);
            newLikeAndDislike.setDislike(0);
            userExamLikeDislikeRepository.save(newLikeAndDislike);
        } else {
            userExamLikeAndDislikeStatus.setLike(1);
            userExamLikeAndDislikeStatus.setDislike(0);
            userExamLikeDislikeRepository.updateLikeAndDislike(userExamLikeAndDislikeStatus);
        }
    }

    // TODO: beallitani a dislike statuszat aktivra es a like statuszat inaktivra
    public void ChangeDislikeStatusToActiveAndLikeToInactive(Long examId, Long userId) {
        UserExamLikeDislike userExamLikeAndDislikeStatus = userExamLikeDislikeRepository.findByUserAndExamsId(userId, examId)
                .orElse(null);

        if (userExamLikeAndDislikeStatus == null) {
            UserExamLikeDislike newLikeAndDislike = new UserExamLikeDislike();
            newLikeAndDislike.setLike(0);
            newLikeAndDislike.setDislike(1);
            userExamLikeDislikeRepository.save(newLikeAndDislike);
        } else {
            userExamLikeAndDislikeStatus.setLike(0);
            userExamLikeAndDislikeStatus.setDislike(1);
            userExamLikeDislikeRepository.updateLikeAndDislike(userExamLikeAndDislikeStatus);
        }
    }

    // TODO: lekerni a like statuszt
    // itt kerem le a like statuszt
    @Cacheable("getLikeStatus")
    public String getLikeStatus(Long examId, Long userId){
        Exams exam_Id = examsRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserExamLikeDislike existinglike = userExamLikeDislikeRepository.findByUserAndExams(user_Id, exam_Id);

        if (existinglike != null) {
            return existinglike.getLike().toString();
        } else {
            return "0";
        }
    }

    // TODO: lekerni a dislike statuszt
    // itt kerem le a dislike statuszt
    @Cacheable("getDislikeStatus")
    public String getDislikeStatus(Long examId, Long userId){
        Exams exam_Id = examsRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserExamLikeDislike existingdislike = userExamLikeDislikeRepository.findByUserAndExams(user_Id, exam_Id);

        if (existingdislike != null){
            //System.out.println("existingdislike: " + existingdislike.getLike());
            return existingdislike.getDislike().toString();
        } else {
            return "0";
        }
    }

    // TODO: lekerni a like es dislike statuszt
    @Cacheable(value = "getLikeAndDislikeStatus")
    public List<UserLikeAndDislikeData> getLikeAndDislikeStatus(Long userId) {
        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        List<ExamsLikeDislikeDTO> userLikeAndDislikes = userExamLikeDislikeRepository.findAllByUserId(user_Id);

        List<UserLikeAndDislikeData> ExamsLikeAndDislikeDataList = new ArrayList<>();

        for (ExamsLikeDislikeDTO userLikeAndDislike : userLikeAndDislikes) {
            Long examId = userLikeAndDislike.getExam_id();
            Long userId1 = userLikeAndDislike.getUser_id();
            int like = userLikeAndDislike.getLike();
            int dislike = userLikeAndDislike.getDislike();
            UserLikeAndDislikeData userLikeAndDislikeData = new UserLikeAndDislikeData(examId, userId1, like, dislike);
            ExamsLikeAndDislikeDataList.add(userLikeAndDislikeData);
        }
        return ExamsLikeAndDislikeDataList;
    }

}
