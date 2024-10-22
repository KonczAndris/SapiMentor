package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.FavoritesDTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.ChatMessageReadOrNot;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ChatMessageReadOrNotRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.FavoriteRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Transactional
public class FavoritesService {
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;
    private final ChatMessageReadOrNotRepository chatMessageReadOrNotRepository;

    public FavoritesService(UserRepository userRepository,
                            FavoriteRepository favoriteRepository,
                            ChatMessageReadOrNotRepository chatMessageReadOrNotRepository) {
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
        this.chatMessageReadOrNotRepository = chatMessageReadOrNotRepository;
    }

    @Cacheable("showAllFavorites")
    public void showAllFavorites(Model model, Principal principal) {
        String email = principal.getName();
        Long userId = userRepository.findIdByEmail(email);
        List<User> allFavorites = favoriteRepository.findAllFavoriteUserByIdAndStatus(userId);

        List<FavoritesDTO> favoriteUserDTOs = new ArrayList<>();
        for (User favoritesUsers : allFavorites) {
            FavoritesDTO favoriteDTO = new FavoritesDTO();
            favoriteDTO.setId(favoritesUsers.getId());
            favoriteDTO.setEmail(favoritesUsers.getEmail());
            favoriteDTO.setFullName(favoritesUsers.getLast_Name() + " " + favoritesUsers.getFirst_Name());
            favoriteDTO.setStatus(favoritesUsers.getStatus());
            byte[] favoriteprofileImage = favoritesUsers.getProfileImage();
            if (favoriteprofileImage != null) {
                String favoritebase64Image = Base64.getEncoder().encodeToString(favoriteprofileImage);
                favoriteDTO.setFavoriteProfileImageBase64(favoritebase64Image);
            }else {
                favoriteDTO.setFavoriteProfileImageBase64("");
            }

            favoriteUserDTOs.add(favoriteDTO);

        }
        //System.out.println("favoriteUserDTOs: " + favoriteUserDTOs.get(0).getFavoriteProfileImageBase64());

        model.addAttribute("allFavoritesUser", favoriteUserDTOs);
    }

    @Cacheable("senderUserImg")
    public List<Object[]> getSenderUserImg(Long userId) {
        return userRepository.findProfileImageById(userId);
    }

    @Cacheable("chatMessageReadOrNot")
    public List<Object[]> getChatMessageReadOrNot(Long recipientId) {
        //System.out.println("recipientId: " + recipientId);
        return chatMessageReadOrNotRepository.findByRecipientId(recipientId);
    }

    public String updateChatMessageReadOrNotStatus(Long senderId, Long recipientId) {
        ChatMessageReadOrNot chatMessageReadOrNot = chatMessageReadOrNotRepository.findBySenderIdAndRecipientId(senderId, recipientId);
        if (chatMessageReadOrNot != null) {
            chatMessageReadOrNot.setReadOrNot(1);
            chatMessageReadOrNotRepository.save(chatMessageReadOrNot);
            return "US";
        } else {
            return "NUS";
        }
    }
}
