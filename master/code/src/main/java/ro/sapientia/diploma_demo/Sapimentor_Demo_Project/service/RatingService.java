package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.RatingDTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Rating;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.RatingRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;


    public RatingService(RatingRepository ratingRepository,
                         UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
    }

    @Cacheable("saveRating")
    public Rating saveRating(String ratingUserEmail,
                             Rating ratingData) {
        try {
            Long ratingUserId = userRepository.findIdByEmail(ratingUserEmail);
            //Long ratedUserId = userRepository.findIdByEmail(ratedUserEmail);
            Long ratedUserId = ratingData.getUserId();
            int score = ratingData.getScore();
            String comment = ratingData.getComment();
            String date = ratingData.getDate();
            Rating existingRating = ratingRepository.findByUserIdAndRatedUserId(ratingUserId, ratedUserId);
            if(existingRating != null) {
                //existingRating.setScore(score);
                //return ratingRepository.save(existingRating);
//                System.out.println("Existing ratingUserId: " + ratingUserId);
//                System.out.println("Existing ratedUserId: " + ratedUserId);
//                System.out.println("Existing score: " + score);
//                System.out.println("Existing comment: " + comment);
//                System.out.println("Existing date: " + date);
                // return null;

                existingRating.setScore(score);
                existingRating.setComment(comment);
                existingRating.setDate(date);
                return ratingRepository.save(existingRating);
            } else {
//                System.out.println("New ratingUserId: " + ratingUserId);
//                System.out.println("New ratedUserId: " + ratedUserId);
//                System.out.println("New score: " + score);
//                System.out.println("New comment: " + comment);
//                System.out.println("New date: " + date);
                // return null;
                Rating newRating = new Rating();
                newRating.setUserId(ratingUserId);
                newRating.setRatedUserId(ratedUserId);
                newRating.setScore(score);
                newRating.setComment(comment);
                newRating.setDate(date);
                return ratingRepository.save(newRating);
//                Rating newRating = new Rating();
//                newRating.setUserId(ratingUserId);
//                newRating.setRatedUserId(ratedUserId);
//                newRating.setScore(score);
                // return ratingRepository.save(newRating);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Cacheable("revokeRating")
    public void revokeRating(String ratingUserEmail,
                               String ratedUserEmail) {
        try {
            Long ratingUserId = userRepository.findIdByEmail(ratingUserEmail);
            Long ratedUserId = userRepository.findIdByEmail(ratedUserEmail);
            ratingRepository.deleteByUserIdAndRatedUserId(ratingUserId, ratedUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Cacheable("getAverageRating")
    public Map<String, Double> getAverageRating(Long ratedUserId) {
        try {
            List<RatingDTO> ratingDTOList = ratingRepository.findByRatedUserId(ratedUserId);
            int sum = 0;
            for (RatingDTO rating : ratingDTOList) {
                sum += rating.getScore();
            }
            double average = (double) sum / ratingDTOList.size();
            Map<String, Double> result = new HashMap<>();
            result.put("average", average);
            result.put("count", (double) ratingDTOList.size());

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }
}
