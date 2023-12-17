package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.MyGroupProfileDetailDTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Rating;
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

    public MyGroupService(UserRepository userRepository,
                          RatingRepository ratingRepository,
                          RatingService ratingService) {
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
        this.ratingService = ratingService;
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
    }

    public void getAllMentorsDetails (Model model, Principal principal) {
        String email = principal.getName();
        Long userId = userRepository.findIdByEmail(email);
        List<MyGroupProfileDetailDTO> allMentors = userRepository.findAllMentors(userId);
        List<Rating> allRatings = ratingRepository.findAllMentorRatingById(userId);

        Map<Long, Double> averageRatingsByUserId = new HashMap<>();
        for (MyGroupProfileDetailDTO user : allMentors) {
            Long user_Id = user.getId();
            double averageRating = ratingService.getAverageRating(user_Id).get("average");
            averageRatingsByUserId.put(user_Id, averageRating);
        }

        model.addAttribute("allMenteesOrMentors", allMentors);
        model.addAttribute("allRatings", allRatings);
        model.addAttribute("averageRatingsByUserId", averageRatingsByUserId);
    }

}
