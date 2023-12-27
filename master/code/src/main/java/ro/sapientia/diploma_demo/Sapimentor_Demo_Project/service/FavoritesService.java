package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.FavoriteRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import javax.transaction.Transactional;
import java.security.Principal;
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
        for (User favoritesUsers : allFavorites) {
            System.out.println("FavoriteUser_Id: " + favoritesUsers.getId()
                    + ", Email:" + favoritesUsers.getEmail()
                    + ", Name: " + favoritesUsers.getLast_Name()
                    + " " +  favoritesUsers.getFirst_Name()
                    + ", Status: " + favoritesUsers.getStatus());
        }
        model.addAttribute("allFavoritesUser", allFavorites);
    }
}
