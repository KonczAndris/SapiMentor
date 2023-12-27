package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config.UserRegistrationDetails;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.MyGroupProfileDetailDTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.*;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.FavoriteRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ProfileTopicsRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.RatingRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MyGroupService {

    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final RatingService ratingService;
    private final FavoriteRepository favoriteRepository;
    private final TopicService topicService;
    private final ProfileTopicsRepository profileTopicsRepository;

    public MyGroupService(UserRepository userRepository,
                          RatingRepository ratingRepository,
                          RatingService ratingService,
                          FavoriteRepository favoriteRepository,
                          TopicService topicService,
                          ProfileTopicsRepository profileTopicsRepository) {

        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
        this.ratingService = ratingService;
        this.favoriteRepository = favoriteRepository;
        this.topicService = topicService;
        this.profileTopicsRepository = profileTopicsRepository;
    }

    @Cacheable("getAllMenteeProfileImageById")
    public List<Object[]> getAllMenteeProfileImageById(Long userId) {
        return userRepository.findallMenteeProfileImageById(userId);
    }

    @Cacheable("getAllMentorProfileImageById")
    public List<Object[]> getAllMentorProfileImageById(Long userId) {
        return userRepository.findallMentorProfileImageById(userId);
    }

    public void getAllMeneesDetails (Model model, Principal principal) {
        String email = principal.getName();
        Long userId = userRepository.findIdByEmail(email);
        List<MyGroupProfileDetailDTO> allMentees = userRepository.findAllMentees(userId);
        List<Rating> allRatings = ratingRepository.findAllMenteeRatingById(userId);
        List<Favorites> allFavorites = favoriteRepository.findAllFavoriteMenteeById(userId);
        //System.out.println("allFavorites: " + allFavorites);
        Map<Long, Integer> favoriteIdsByUserId = new HashMap<>();
        for (Favorites favorite : allFavorites) {
            //System.out.println("Favorites id: " + favorite.getId() + ", Status: " + favorite.getStatus() + ", User_id: " + favorite.getUser_id() + ", Favorite_id: " + favorite.getFavorite_id());
            favoriteIdsByUserId.put(favorite.getFavorite_id(), favorite.getStatus());
        }

        // Map<Long, String> profileImagesByUserId = new HashMap<>();
        Map<Long, Double> averageRatingsByUserId = new HashMap<>();
        for (MyGroupProfileDetailDTO user : allMentees) {
            Long user_Id = user.getId();
            double averageRating = ratingService.getAverageRating(user_Id).get("average");
            averageRatingsByUserId.put(user_Id, averageRating);
        }


        model.addAttribute("allMenteesOrMentors", allMentees);
        model.addAttribute("allRatings", allRatings);
        model.addAttribute("averageRatingsByUserId", averageRatingsByUserId);
        model.addAttribute("favoriteIdsByUserId", favoriteIdsByUserId);
    }

    public void getAllMentorsDetails (Model model, Principal principal) {
        String email = principal.getName();
        Long userId = userRepository.findIdByEmail(email);
        List<MyGroupProfileDetailDTO> allMentors = userRepository.findAllMentors(userId);
        List<Rating> allRatings = ratingRepository.findAllMentorRatingById(userId);
        List<Favorites> allFavorites = favoriteRepository.findAllFavoriteMentorById(userId);
        System.out.println("allFavorites: " + allFavorites);
        Map<Long, Integer> favoriteIdsByUserId = new HashMap<>();
        for (Favorites favorite : allFavorites) {
            System.out.println("Favorites id: " + favorite.getId() + ", Status: " + favorite.getStatus() + ", User_id: " + favorite.getUser_id() + ", Favorite_id: " + favorite.getFavorite_id());
            favoriteIdsByUserId.put(favorite.getFavorite_id(), favorite.getStatus());
        }

        Map<Long, Double> averageRatingsByUserId = new HashMap<>();
        for (MyGroupProfileDetailDTO user : allMentors) {
            Long user_Id = user.getId();
            double averageRating = ratingService.getAverageRating(user_Id).get("average");
            averageRatingsByUserId.put(user_Id, averageRating);
        }

        model.addAttribute("allMenteesOrMentors", allMentors);
        model.addAttribute("allRatings", allRatings);
        model.addAttribute("allFavorites", allFavorites);
        model.addAttribute("averageRatingsByUserId", averageRatingsByUserId);
        model.addAttribute("favoriteIdsByUserId", favoriteIdsByUserId);
    }

    @Cacheable("saveFavorite")
    public void  saveFavorite(Principal principal,Long favoriteUserId) {
        String email = principal.getName();
        Long user_Id = userRepository.findIdByEmail(email);
        Favorites existingFavorite = favoriteRepository.findByUserIdAndFavoriteId(user_Id,favoriteUserId);
        if(existingFavorite != null){
            existingFavorite.setStatus(1);
            favoriteRepository.save(existingFavorite);
        } else {
            Favorites favorite = new Favorites();
            favorite.setUser_id(user_Id);
            favorite.setFavorite_id(favoriteUserId);
            favorite.setStatus(1);
            favoriteRepository.save(favorite);
        }
    }

    @Cacheable("revokeFavorite")
    public void revokeFavorite(Principal principal,Long favoriteUserId) {
        String email = principal.getName();
        Long user_Id = userRepository.findIdByEmail(email);
        Favorites existingFavorite = favoriteRepository.findByUserIdAndFavoriteId(user_Id,favoriteUserId);
        if(existingFavorite != null){
            existingFavorite.setStatus(0);
            favoriteRepository.save(existingFavorite);
        } else {
            Favorites favorite = new Favorites();
            favorite.setUser_id(user_Id);
            favorite.setFavorite_id(favoriteUserId);
            favorite.setStatus(0);
            favoriteRepository.save(favorite);
        }
    }

    @Cacheable("getSelectedUserProfile")
    public void getSelectedUserProfile(Long userId,Model model) {
        User user = userRepository.findUserById(userId);
        System.out.println("Email: " + user.getEmail()
        + ", Name: " + user.getFirst_Name() + " " + user.getLast_Name() );

        UserRegistrationDetails userRegistrationDetails = new UserRegistrationDetails(user);
        System.out.println("userRegistrationDetails: " + userRegistrationDetails.getUsername());

        List<Topic> topics = topicService.getAllTopics();

        List<Profile_Topics> userTopics = profileTopicsRepository.findByUserId(userId);

        if (!userTopics.isEmpty()) {
            System.out.println("User topics: " + userTopics);
            System.out.println("User topics: " + userTopics.get(0).getTags());
            for (Profile_Topics profile_topics : userTopics) {
                System.out.println("Profile_topics: " + profile_topics.getTags());
            }
            //model.addAttribute("userTopics", userTopics);
        }
    }

}
