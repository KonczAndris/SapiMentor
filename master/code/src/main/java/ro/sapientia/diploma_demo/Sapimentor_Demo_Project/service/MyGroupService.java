package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.MyGroupProfileDetailDTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.UserDetailsDTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.*;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.FavoriteRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ProfileTopicsRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.RatingRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.*;

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

//    public List<Object[]> getSelectedUserImages(Long userId) {
//        List<Rating> allRatingsForThisUser = ratingRepository.findAllByUserId(userId);
//        System.out.println("allRatingsForThisUser: " + allRatingsForThisUser);
//        ArrayList<Long> allUserId = new ArrayList<>();
//        for (Rating rating : allRatingsForThisUser) {
//            allUserId.add(rating.getUserId());
//        }
//        List<User> allSelectedUsers = userRepository.findAllByIdIn(allUserId);
//        return userRepository.findAllSelectedUserImages(allSelectedUsers);
//    }

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

//    @Cacheable("getSelectedUserProfile")
    public void getSelectedUserProfile(Long userId,Model model) {
        User user = userRepository.findUserById(userId);
        UserDetailsDTO userDetailsForMyGroup = new UserDetailsDTO(user);

        byte[] profileImageForMyGroup = user.getProfileImage();
        if (profileImageForMyGroup != null) {
            String profileImageForMyGroupBase64 = Base64.getEncoder().encodeToString(profileImageForMyGroup);
            model.addAttribute("profileImageForMyGroupBase64", profileImageForMyGroupBase64);
        } else {
            model.addAttribute("profileImageForMyGroupBase64", "");
        }

        double averageRatingForMyGruopModal = ratingService.getAverageRating(userId).get("average");
        model.addAttribute("averageRatingForMyGruopModal", averageRatingForMyGruopModal);
        model.addAttribute("userDetailsForMyGroup", userDetailsForMyGroup);
        List<Topic> topics = topicService.getAllTopics();
        List<Profile_Topics> userTopicsForMyGroup = profileTopicsRepository.findByUserId(userId);
        model.addAttribute("uId", userId);

        if (!userTopicsForMyGroup.isEmpty()) {
            model.addAttribute("userTopicsForMyGroup", userTopicsForMyGroup);
        }


    }

    public void getSelectedUserComments(Long  userId,Model model, Principal principal) {
        List<Rating> allRatingsForThisUser = ratingRepository.findAllByUserId(userId);
        ArrayList<Long> allUserId = new ArrayList<>();
        for (Rating rating : allRatingsForThisUser) {
//            System.out.println("RatedUserId: " + rating.getRatedUserId() +
//                    ", Score: " + rating.getScore() +
//                    ", Comment: " + rating.getComment() +
//                    ", Date: " + rating.getDate() +
//                    ", Who rate : " + rating.getUserId());
            allUserId.add(rating.getUserId());
        }

        List<User> allSelectedUsers = userRepository.findAllByIdIn(allUserId);
//        for (User user : allSelectedUsers) {
//            System.out.println("User: " + user.getFirst_Name() + " " + user.getLast_Name());
//        }

        model.addAttribute("allSelectedUsers", allSelectedUsers);

        String email = principal.getName();
        Long user_Id = userRepository.findIdByEmail(email);
        Rating ratingForThisUser = ratingRepository.findByUserIdAndRatedUserId(user_Id,userId);

        if (ratingForThisUser != null) {
            model.addAttribute("ratingForThisUser", ratingForThisUser);
        }
        model.addAttribute("allRatingsForThisUser", allRatingsForThisUser);

    }


    ////////////////////////////////////////////////////////////////
    // Filteres kereses
    ////////////////////////////////////////////////////////////////

    @Cacheable("getAllMenteesDetailsByFilter")
    public void getAllMenteesDetailsByFilter(Principal principal, Model model, MultiValueMap<String, String> params){
        String email = principal.getName();
        Long userId = userRepository.findIdByEmail(email);
        List<MyGroupProfileDetailDTO> allMentees = userRepository.findAllMentees(userId);
        List<MyGroupProfileDetailDTO> filteredUsers = new ArrayList<>();
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

        for (MyGroupProfileDetailDTO userDTO : allMentees) {

            Long userDTOId = userDTO.getId();
            System.out.println("User: " + userDTO.getFirstName() + " " + userDTO.getLastName() + ", id: " + userDTO.getId());
            List<Profile_Topics> userTopics = profileTopicsRepository.findByUserId(userDTOId);

            boolean userMatchesFilter = true;

            for (Map.Entry<String, List<String>> entry : params.entrySet()) {
                String paramName = entry.getKey();
                List<String> paramValues = new ArrayList<>();
                paramValues = Arrays.asList(entry.getValue().get(0).split(","));

                System.out.println("ELSO paramName: " + paramName + ", paramValues: " + paramValues);

                for (Profile_Topics profile_topics : userTopics) {
                    System.out.println("Profile_topics: " +profile_topics.getTopic() + ": " + profile_topics.getTags());

                    if(paramName.equals(profile_topics.getTopic()) ) {
                        System.out.println("Egyenlo a topic a felhasznalo topicjaval");


                        if (!filteredUsers.contains(userDTO)) {
                            filteredUsers.add(userDTO);
                        }
                        System.out.println("filteredUsers123: " + filteredUsers);
                        int db = 0;
                        List<String> emptyStringList = new ArrayList<>();

                        System.out.println("paramValues.size(): " + paramValues.get(0).length());
                        if (paramValues.get(0).length() != 0 ){

                            for (String paramValue : paramValues) {

                                System.out.println("paramValueasdasd: " + paramValue);
                                emptyStringList.add(paramValue);
                                if (profile_topics.getTags().contains(paramValue)) {
                                    db++;
                                    System.out.println("Nem tartalmazza a taget");
                                    //filteredUsers.remove(userDTO);
                                }
                            }
                            System.out.println("emptyStringList: " + emptyStringList.get(0).length());
                            if (db == 0 ){
                                filteredUsers.remove(userDTO);
                            }
                            System.out.println("db: " + db);
                        }

                    }
                }
                System.out.println("userMatchesFilter: " + userMatchesFilter);
            }
        }

        //System.out.println("filteredUsers: " + filteredUsers);
//        for (MyGroupProfileDetailDTO userDTO : filteredUsers) {
//
//            System.out.println("Userutolso: " + userDTO.getFirstName() + " " + userDTO.getLastName() + ", id: " + userDTO.getId());
//        }

        model.addAttribute("allMenteesOrMentors", filteredUsers);
        model.addAttribute("allRatings", allRatings);
        model.addAttribute("averageRatingsByUserId", averageRatingsByUserId);
        model.addAttribute("favoriteIdsByUserId", favoriteIdsByUserId);
    }

    @Cacheable("getAllMentorsDetailsByFilter")
    public void getAllMentorsDetailsByFilter(Principal principal, Model model, MultiValueMap<String, String> params){
        String email = principal.getName();
        Long userId = userRepository.findIdByEmail(email);
        List<MyGroupProfileDetailDTO> allMentors = userRepository.findAllMentors(userId);
        List<MyGroupProfileDetailDTO> filteredUsers = new ArrayList<>();
        List<Rating> allRatings = ratingRepository.findAllMentorRatingById(userId);
        List<Favorites> allFavorites = favoriteRepository.findAllFavoriteMentorById(userId);
        //System.out.println("allFavorites: " + allFavorites);
        Map<Long, Integer> favoriteIdsByUserId = new HashMap<>();
        for (Favorites favorite : allFavorites) {
            //System.out.println("Favorites id: " + favorite.getId() + ", Status: " + favorite.getStatus() + ", User_id: " + favorite.getUser_id() + ", Favorite_id: " + favorite.getFavorite_id());
            favoriteIdsByUserId.put(favorite.getFavorite_id(), favorite.getStatus());
        }

        // Map<Long, String> profileImagesByUserId = new HashMap<>();
        Map<Long, Double> averageRatingsByUserId = new HashMap<>();
        for (MyGroupProfileDetailDTO user : allMentors) {
            Long user_Id = user.getId();
            double averageRating = ratingService.getAverageRating(user_Id).get("average");
            averageRatingsByUserId.put(user_Id, averageRating);
        }

        for (MyGroupProfileDetailDTO userDTO : allMentors) {

            Long userDTOId = userDTO.getId();
            System.out.println("User: " + userDTO.getFirstName() + " " + userDTO.getLastName() + ", id: " + userDTO.getId());
            List<Profile_Topics> userTopics = profileTopicsRepository.findByUserId(userDTOId);

            boolean userMatchesFilter = true;

            for (Map.Entry<String, List<String>> entry : params.entrySet()) {
                String paramName = entry.getKey();
                List<String> paramValues = new ArrayList<>();
                paramValues = Arrays.asList(entry.getValue().get(0).split(","));

                System.out.println("ELSO paramName: " + paramName + ", paramValues: " + paramValues);

                for (Profile_Topics profile_topics : userTopics) {
                    System.out.println("Profile_topics: " +profile_topics.getTopic() + ": " + profile_topics.getTags());

                    if(paramName.equals(profile_topics.getTopic()) ) {
                        System.out.println("Egyenlo a topic a felhasznalo topicjaval");


                        if (!filteredUsers.contains(userDTO)) {
                            filteredUsers.add(userDTO);
                        }
                        System.out.println("filteredUsers123: " + filteredUsers);
                        int db = 0;
                        List<String> emptyStringList = new ArrayList<>();

                        System.out.println("paramValues.size(): " + paramValues.get(0).length());
                        if (paramValues.get(0).length() != 0 ){

                            for (String paramValue : paramValues) {

                                System.out.println("paramValueasdasd: " + paramValue);
                                emptyStringList.add(paramValue);
                                if (profile_topics.getTags().contains(paramValue)) {
                                    db++;
                                    System.out.println("Nem tartalmazza a taget");
                                    //filteredUsers.remove(userDTO);
                                }
                            }
                            System.out.println("emptyStringList: " + emptyStringList.get(0).length());
                            if (db == 0 ){
                                filteredUsers.remove(userDTO);
                            }
                            System.out.println("db: " + db);
                        }

                    }
                }
                System.out.println("userMatchesFilter: " + userMatchesFilter);
            }
        }

        //System.out.println("filteredUsers: " + filteredUsers);
//        for (MyGroupProfileDetailDTO userDTO : filteredUsers) {
//
//            System.out.println("Userutolso: " + userDTO.getFirstName() + " " + userDTO.getLastName() + ", id: " + userDTO.getId());
//        }

        model.addAttribute("allMenteesOrMentors", filteredUsers);
        model.addAttribute("allRatings", allRatings);
        model.addAttribute("averageRatingsByUserId", averageRatingsByUserId);
        model.addAttribute("favoriteIdsByUserId", favoriteIdsByUserId);
    }


}


