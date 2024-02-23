package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.*;

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
    private final RoleRepository roleRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final FavoriteRepository favoriteRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageReadOrNotRepository chatMessageReadOrNotRepository;
    private final RatingRepository ratingRepository;
    private final ProfileTopicsRepository profileTopicsRepository;


    public String deactivateUserAccount(Principal principal){
        String email = principal.getName();
        Long userId = userRepository.findIdByEmail(email);

        try {
            userResourceLikeDislikeRepository.deleteByUserId(userId);
            userExamLikeDislikeRepository.deleteByUserId(userId);
            userDiplomaLikeDislikeRepository.deleteByUserId(userId);
            userRepository.deleteUserRoleRecordsByUserId(userId);

            roleRepository.getAllRole_idByUserId(userId).forEach(role -> {
                //System.out.println("Rolek: " + role[0]);
                roleRepository.updateRolesStatusByIdCustom((Integer) role[0]);
            });
            passwordResetTokenRepository.deleteByUserId(userId);
            confirmationTokenRepository.deleteByUserId(userId);
            favoriteRepository.deleteAllUserById(userId);
            chatMessageRepository.deleteByUserId(userId);
            chatRoomRepository.deleteBySenderIdOrRecipientId(userId);
            chatMessageReadOrNotRepository.deleteBySenderIdOrRecipientId(userId);
            ratingRepository.deleteAllByUserId(userId);
            profileTopicsRepository.findPTIdByUserId(userId).forEach(pt -> {
                //System.out.println("ProfileTopics: " + pt[0]);
                profileTopicsRepository.deleteById((Long) pt[0]);
            });

            profileTopicsRepository.deleteByUserId(userId);
            userRepository.deleteUserById(userId);

            //System.out.println("UserResourceLikeDislike deleted");
            return "DELETED";
        } catch (Exception e){
            return "ERROR";
        }
    }
}
