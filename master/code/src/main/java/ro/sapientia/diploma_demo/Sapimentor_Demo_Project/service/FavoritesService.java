package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.FavoritesDTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
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

    public FavoritesService(UserRepository userRepository,
                            FavoriteRepository favoriteRepository) {
        this.userRepository = userRepository;
        this.favoriteRepository = favoriteRepository;
    }

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

    public List<Object[]> getSenderUserImg(Long userId) {
        return userRepository.findProfileImageById(userId);
    }
}
