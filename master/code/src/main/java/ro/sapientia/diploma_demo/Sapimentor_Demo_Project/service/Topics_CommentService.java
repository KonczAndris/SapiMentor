package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Topics_Comment;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.Topics_CommentRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class Topics_CommentService {

    private final UserRepository userRepository;
    private final Topics_CommentRepository topicsCommentRepository;

    public Topics_CommentService(UserRepository userRepository,
                                 Topics_CommentRepository topicsCommentRepository) {
        this.userRepository = userRepository;
        this.topicsCommentRepository = topicsCommentRepository;
    }
    @Cacheable("saveTopicComment")
    public Topics_Comment saveTopicComment(String userEmail,
                                           Topics_Comment topicsComment) {
        try {
            Long userId = userRepository.findIdByEmail(userEmail);
            String ratedTopicId = topicsComment.getRatedTopicId();
            String subject = topicsComment.getSubject();
            String subject_hu = topicsComment.getSubject_hu();
            String comment = topicsComment.getComment();
            String date = topicsComment.getDate();
                Topics_Comment newComment = new Topics_Comment();
                newComment.setUserId(userId);
                newComment.setRatedTopicId(ratedTopicId);
                newComment.setSubject(subject);
                newComment.setSubject_hu(subject_hu);
                newComment.setComment(comment);
                newComment.setDate(date);
                return topicsCommentRepository.save(newComment);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Cacheable("getSelectedTopicComments")
    public void getSelectedTopicComments(String ratedTopicId,
                                         Model model,
                                         Principal principal) {
        List<Topics_Comment> allCommentsForThisTopic = topicsCommentRepository.findAllByRatedTopicId(ratedTopicId);
        ArrayList<Long> allUserId = new ArrayList<>();
        for (Topics_Comment comment : allCommentsForThisTopic) {
            allUserId.add(comment.getUserId());
        }

        List<User> allSelectedUsers = userRepository.findAllByIdIn(allUserId);

        model.addAttribute("allSelectedUsers", allSelectedUsers);

        String email = principal.getName();
        Long userId = userRepository.findIdByEmail(email);
        Topics_Comment commentForThisTopic = topicsCommentRepository.findByUserIdAndRatedTopicId(userId, ratedTopicId);
        if (commentForThisTopic != null) {
            model.addAttribute("commentForThisTopic", commentForThisTopic);
        }

        model.addAttribute("allCommentsForThisTopic", allCommentsForThisTopic);
    }

    public List<Object[]> getSelectedUserImages(String topicId) {
        List<Topics_Comment> allRatingsForThisUserImages = topicsCommentRepository.findAllByRatedUserId(topicId);
        //System.out.println("allRatingsForThisUser: " + allRatingsForThisUserImages);
        ArrayList<Long> allUserId = new ArrayList<>();
        for (Topics_Comment comment : allRatingsForThisUserImages) {
//            System.out.println("RatedUserId: " + rating.getRatedUserId() +
//                    ", Score: " + rating.getScore() +
//                    ", Comment: " + rating.getComment() +
//                    ", Date: " + rating.getDate() +
//                    ", Who rate : " + rating.getUserId());
            allUserId.add(comment.getUserId());
        }
        List<Object[]> allSelectedUsers = userRepository.findAllSelectedUserImages(allUserId);

//        for (Object[] user : allSelectedUsers) {
//            System.out.println("User: " + user[0] + ", id: " + user[1]);
//        }

        return allSelectedUsers;
    }
}