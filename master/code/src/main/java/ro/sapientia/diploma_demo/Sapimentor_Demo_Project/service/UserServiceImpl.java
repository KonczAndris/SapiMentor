package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;


import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.UserRegistrationDto;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.token.ConfirmationToken;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Rating;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Role;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ConfirmationTokenRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.RatingRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

//megkerdezni, hogy ez miert kell!!!!!
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RatingRepository ratingRepository;

    //Ezzel tudod beallitani hogy mekkora legyen a maximalis meret amit feltolthet a felhasznalo
    private static final long MAX_IMAGE_SIZE = 2 * 1024 * 1024; // 2 MB
    //private static final long MAX_IMAGE_SIZE = 10 * 1024; // 10 KB
    //private static final long MAX_IMAGE_SIZE = 20 * 1024; // 20 KB
    //private static final long MAX_IMAGE_SIZE = 40 * 1024; // 40 KB

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           ConfirmationTokenRepository confirmationTokenRepository,
                           RatingRepository ratingRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.ratingRepository = ratingRepository;
    }



    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    //ez a regisztraciohoz kell
    @Override
    public User save(UserRegistrationDto registrationDto) {
        User user = new User(registrationDto.getFirstName(),
                registrationDto.getLastName(), registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()), Arrays.asList(new Role("USER")));
        return userRepository.save(user);
    }

    public void saveUserVerificationToken(User theUser, String token){
        var verificationToken = new ConfirmationToken(token, theUser);
        confirmationTokenRepository.save(verificationToken);
    }

    @Override
    public String validateToken(String theToken) {
        ConfirmationToken token = confirmationTokenRepository.findByToken(theToken);
        if(token == null){
            return "Invalid verification token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime()- calendar.getTime().getTime()) <= 0){
            confirmationTokenRepository.delete(token);
            userRepository.delete(user);
            return "Token already expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Transactional
    public void saveProfileData(String email,
                                String firstName,
                                String lastName,
                                String specialization,
                                Integer year,
                                String phoneNumber) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            System.out.println( phoneNumber);
            user.setFirst_Name(firstName);
            user.setLast_Name(lastName);
            user.setSpecialization(specialization);
            user.setYear(year);
            user.setPhone(phoneNumber);

            userRepository.save(user);
        }
    }

    public boolean isAtLeastSecondYear(Long userId){
        User user = userRepository.findById(userId).orElse(null);
        Integer year = user.getYear();
        return year != null && year >= 1;
    }



    public String uploadProfileImage(String email, MultipartFile image) {
        User user = userRepository.findByEmail(email);
        if (user != null && !image.isEmpty() ) {

            try {
                //System.out.println("Image size: " + image.getSize());
                //System.out.println("MAX_IMAGE_SIZE: " + MAX_IMAGE_SIZE);
                //A regi verzional ez a sor nem kell !!!!
                if (image.getSize() > MAX_IMAGE_SIZE) {
                    return "The uploaded image is too large. Please choose a smaller image.";
                }


                byte[] originalImageBytes = image.getBytes();
                // Skálázom a képet a megadott méretekre
                BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalImageBytes));

                // ez az uj verzio
                if (originalImage != null) {
                    int minDimension = Math.min(originalImage.getWidth(), originalImage.getHeight());
                    int x = (originalImage.getWidth() - minDimension) / 2;
                    int y = (originalImage.getHeight() - minDimension) / 2;

                    BufferedImage squareImage = originalImage.getSubimage(x, y, minDimension, minDimension);

                    BufferedImage scaledImage = Thumbnails.of(squareImage)
                            .size(600, 600)
                            .outputQuality(0.6)
                            .asBufferedImage();

                    //Be allitom a kimeneti fájltípust (pl. jpg)
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(scaledImage, "jpg", baos);
                    byte[] scaledImageBytes = baos.toByteArray();

                    if (scaledImageBytes.length == 0) {
                        return "Wrong type";
                    } else {
                        user.setProfileImage(scaledImageBytes);
                    }
                }
                // ez a regiverzio
//                int minDimension = Math.min(originalImage.getWidth(), originalImage.getHeight());
//                int x = (originalImage.getWidth() - minDimension) / 2;
//                int y = (originalImage.getHeight() - minDimension) / 2;
//
//                BufferedImage squareImage = originalImage.getSubimage(x, y, minDimension, minDimension);
//
//                BufferedImage scaledImage = Thumbnails.of(squareImage)
//                        .size(400, 400)
//                        .outputQuality(0.7)
//                        .asBufferedImage();
//
//                //Be allitom a kimeneti fájltípust (pl. jpg)
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                ImageIO.write(scaledImage, "jpg", baos);
//                byte[] scaledImageBytes = baos.toByteArray();
//
//
//                user.setProfileImage(scaledImageBytes);
                userRepository.save(user);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //A regi verzional ez a sor nem kell !!!!
        return null;
    }


    public void getUserComments(Model model, Principal principal) {
        String email = principal.getName();
        Long userId = userRepository.findIdByEmail(email);
        List<Rating> allRatingsForThisUser = ratingRepository.findAllByRatedUserId(userId);
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
        model.addAttribute("allSelectedUsers", allSelectedUsers);
        model.addAttribute("allRatingsForThisUser", allRatingsForThisUser);
    }

    public List<Object[]> getSelectedUserImagesForProfilePage (Principal principal) {
        String email = principal.getName();
        Long userId = userRepository.findIdByEmail(email);
        List<Rating> allRatingsForThisUserImages = ratingRepository.findAllByRatedUserId(userId);

        ArrayList<Long> allUserId = new ArrayList<>();
        for (Rating rating : allRatingsForThisUserImages) {
            allUserId.add(rating.getUserId());
        }

        List<Object[]> allSelectedUsers = userRepository.findAllSelectedUserImages(allUserId);

        return allSelectedUsers;
    }

}
