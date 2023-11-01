package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Exams;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.UserExamLikeDislike;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.UserLikeAndDislikeData;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ExamsRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserExamLikeDislikeRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
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
        Exams exam_Id = examsRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserExamLikeDislike existingactivelike = userExamLikeDislikeRepository.findByUserAndExams(user_Id, exam_Id);

        if (existingactivelike == null){
            UserExamLikeDislike newactivelike = new UserExamLikeDislike();
            newactivelike.setExams(exam_Id);
            newactivelike.setUser(user_Id);
            newactivelike.setLike(1);
            userExamLikeDislikeRepository.save(newactivelike);
        } else {
            existingactivelike.setLike(1);
            userExamLikeDislikeRepository.save(existingactivelike);
        }
    }

    // TODO: beallitani a like statuszat inaktivra
    // itt allitom at a like statuszt inaktivra vagyis 0-ra
    public void ChangeLikeStatusToInactive(Long examId, Long userId) {
        Exams exam_Id = examsRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserExamLikeDislike existinginactivelike = userExamLikeDislikeRepository.findByUserAndExams(user_Id, exam_Id);

        if (existinginactivelike == null){
            UserExamLikeDislike newinactivelike = new UserExamLikeDislike();
            newinactivelike.setExams(exam_Id);
            newinactivelike.setUser(user_Id);
            newinactivelike.setLike(0);
            userExamLikeDislikeRepository.save(newinactivelike);
        } else {
            existinginactivelike.setLike(0);
            userExamLikeDislikeRepository.save(existinginactivelike);
        }
    }

    // TODO: beallitani a dislike statuszat aktivra
    // itt allitom at a dislike statuszt aktivra vagyis 1-re
    public void ChangeDislikeStatusToActive(Long exmaId, Long userId) {
        Exams exam_Id = examsRepository.findById(exmaId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + exmaId));

        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserExamLikeDislike existingactivedislike = userExamLikeDislikeRepository.findByUserAndExams(user_Id, exam_Id);

        if (existingactivedislike == null){
            UserExamLikeDislike newactivedislike = new UserExamLikeDislike();
            newactivedislike.setExams(exam_Id);
            newactivedislike.setUser(user_Id);
            newactivedislike.setDislike(1);
            userExamLikeDislikeRepository.save(newactivedislike);
        } else {
            existingactivedislike.setDislike(1);
            userExamLikeDislikeRepository.save(existingactivedislike);
        }
    }

    // TODO: beallitani a dislike statuszat inaktivra
    //itt allitom at a dislike statuszt inaktivra vagyis 0-ra
    public void ChangeDislikeStatusToInactive(Long exmaId, Long userId) {
        Exams exam_Id = examsRepository.findById(exmaId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + exmaId));

        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserExamLikeDislike existinginactivedislike = userExamLikeDislikeRepository.findByUserAndExams(user_Id, exam_Id);

        if (existinginactivedislike == null ){
            UserExamLikeDislike newinactivedislike = new UserExamLikeDislike();
            newinactivedislike.setExams(exam_Id);
            newinactivedislike.setUser(user_Id);
            newinactivedislike.setDislike(0);
            userExamLikeDislikeRepository.save(newinactivedislike);
        } else {
            existinginactivedislike.setDislike(0);
            userExamLikeDislikeRepository.save(existinginactivedislike);
        }
    }

    // TODO: beallitani a like statuszat aktivra es a dislike statuszat inaktivra
    public void ChangeLikeStatusToActiveAndDislikeToInactive(Long examId, Long userId) {
        Exams exam_Id = examsRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserExamLikeDislike existingLikeAndDislike = userExamLikeDislikeRepository.findByUserAndExams(user_Id, exam_Id);

        if (existingLikeAndDislike == null){
            UserExamLikeDislike newLikeAndDislike = new UserExamLikeDislike();
            newLikeAndDislike.setExams(exam_Id);
            newLikeAndDislike.setUser(user_Id);
            newLikeAndDislike.setLike(1);
            newLikeAndDislike.setDislike(0);
            userExamLikeDislikeRepository.save(newLikeAndDislike);
        } else {
            existingLikeAndDislike.setLike(1);
            existingLikeAndDislike.setDislike(0);
            userExamLikeDislikeRepository.save(existingLikeAndDislike);
        }
    }

    // TODO: beallitani a dislike statuszat aktivra es a like statuszat inaktivra
    public void ChangeDislikeStatusToActiveAndLikeToInactive(Long examId, Long userId) {
        Exams exam_Id = examsRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + examId));

        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        UserExamLikeDislike existingLikeAndDislike = userExamLikeDislikeRepository.findByUserAndExams(user_Id, exam_Id);

        if (existingLikeAndDislike == null) {
            UserExamLikeDislike newLikeAndDislike = new UserExamLikeDislike();
            newLikeAndDislike.setUser(user_Id);
            newLikeAndDislike.setExams(exam_Id);
            newLikeAndDislike.setLike(0);
            newLikeAndDislike.setDislike(1);
            userExamLikeDislikeRepository.save(newLikeAndDislike);
        } else {
            existingLikeAndDislike.setLike(0);
            existingLikeAndDislike.setDislike(1);
            userExamLikeDislikeRepository.save(existingLikeAndDislike);
        }
    }

    // TODO: lekerni a like statuszt
    // itt kerem le a like statuszt
    @Cacheable(value = "likeStatus")
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
    @Cacheable(value = "dislikeStatus")
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
    public List<UserLikeAndDislikeData> getLikeAndDislikeStatus(Long userId) {
        User user_Id = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        List<UserExamLikeDislike> userLikeAndDislikes = userExamLikeDislikeRepository.findAllByUser(user_Id);

        List<UserLikeAndDislikeData> ExamsLikeAndDislikeDataList = new ArrayList<>();

        for (UserExamLikeDislike userLikeAndDislike : userLikeAndDislikes) {
            Long examId = userLikeAndDislike.getExams().getId();
            Long userId1 = userLikeAndDislike.getUser().getId();
            int like = userLikeAndDislike.getLike();
            int dislike = userLikeAndDislike.getDislike();
            UserLikeAndDislikeData userLikeAndDislikeData = new UserLikeAndDislikeData(examId, userId1, like, dislike);
            ExamsLikeAndDislikeDataList.add(userLikeAndDislikeData);
        }
        return ExamsLikeAndDislikeDataList;
    }

}
