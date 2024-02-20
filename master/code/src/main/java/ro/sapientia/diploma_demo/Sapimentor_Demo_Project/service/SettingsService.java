package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserDiplomaLikeDislikeRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserExamLikeDislikeRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserResourceLikeDislikeRepository;

import javax.transaction.Transactional;
import java.security.Principal;

@Service
@Transactional
@AllArgsConstructor
public class SettingsService {
    private final UserResourceLikeDislikeRepository userResourceLikeDislikeRepository;
    private final UserRepository userRepository;
    private final UserExamLikeDislikeRepository userExamLikeDislikeRepository;
    private final UserDiplomaLikeDislikeRepository userDiplomaLikeDislikeRepository;


    public String deactivateUserAccount(Principal principal){
        String email = principal.getName();
        Long userId = userRepository.findIdByEmail(email);

        //try {
//            userResourceLikeDislikeRepository.deleteByUserId(userId);
//            userExamLikeDislikeRepository.deleteByUserId(userId);
//            userDiplomaLikeDislikeRepository.deleteByUserId(userId);
        userRepository.deleteUserRoleRecordsByUserId(userId);


            System.out.println("UserResourceLikeDislike deleted");
            return "DELETED";
        //} catch (Exception e){
       //     return "ERROR";
       // }
    }
}
