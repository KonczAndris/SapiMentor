package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;


import lombok.RequiredArgsConstructor;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.UserRegistrationDto;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.token.ConfirmationToken;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Role;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ConfirmationTokenRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    //Ezzel tudod beallitani hogy mekkora legyen a maximalis meret amit feltolthet a felhasznalo
    private static final long MAX_IMAGE_SIZE = 2 * 1024 * 1024; // 2 MB
    //private static final long MAX_IMAGE_SIZE = 10 * 1024; // 10 KB
    //private static final long MAX_IMAGE_SIZE = 20 * 1024; // 20 KB
    //private static final long MAX_IMAGE_SIZE = 40 * 1024; // 40 KB

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, ConfirmationTokenRepository confirmationTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenRepository = confirmationTokenRepository;
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


//    public void uploadProfileImage(String email, MultipartFile image) {
//        User user = userRepository.findByEmail(email);
//        if (user != null && !image.isEmpty()) {
//            try {
//                String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
//                Path filePath = Paths.get("/Users/szotyoricsongor-botond/Documents/GitHub/SapiMentor/master/code/src/main/java/ro/sapientia/diploma_demo/Sapimentor_Demo_Project/profile_images",
//                        fileName); // Cseréld le a helyes elérési útra
//                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//                user.setProfileImage(fileName);
//                userRepository.save(user);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }



//    public void uploadProfileImage(String email, MultipartFile image) {
//        User user = userRepository.findByEmail(email);
//        if (user != null && !image.isEmpty()) {
//            try {
//
//                String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
//                Path filePath = Paths.get("src/main/resources/static/profileImg", fileName);
//                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//                user.setProfileImage(fileName);
//                userRepository.save(user);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


//    public void uploadProfileImage(String email, MultipartFile image) {
//        User user = userRepository.findByEmail(email);
//        if (user != null && !image.isEmpty()) {
//            try {
//                String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
//                Path filePath = Paths.get("src/main/resources/static/profileImg", fileName);
//                System.out.println("Saving image to: " + filePath); // Logolás a mentési elérési útvonallal
//                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//                user.setProfileImage(fileName);
//                userRepository.save(user);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }


//    public void uploadProfileImage(String email, MultipartFile image) {
//        User user = userRepository.findByEmail(email);
//        if (user != null && !image.isEmpty()) {
//            try {
//                byte[] imageBytes = image.getBytes();
//
//                user.setProfileImage(imageBytes);
//                userRepository.save(user);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public String uploadProfileImage(String email, MultipartFile image) {
        User user = userRepository.findByEmail(email);
        if (user != null && !image.isEmpty() ) {

            try {
                System.out.println("Image size: " + image.getSize());
                System.out.println("MAX_IMAGE_SIZE: " + MAX_IMAGE_SIZE);
                //A regi verzional ez a sor nem kell !!!!
                if (image.getSize() > MAX_IMAGE_SIZE) {
                    return "The uploaded image is too large. Please choose a smaller image.";
                }


                byte[] originalImageBytes = image.getBytes();
                // Skálázd a képet a megadott méretekre
                BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalImageBytes));
                BufferedImage scaledImage = Thumbnails.of(originalImage)
                        .size(500, 500)
                        .asBufferedImage();

                // Állítsd be a kimeneti fájltípust (pl. JPEG)
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(scaledImage, "jpg", baos);
                byte[] scaledImageBytes = baos.toByteArray();


                user.setProfileImage(scaledImageBytes);
                userRepository.save(user);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //A regi verzional ez a sor nem kell !!!!
        return null;
    }
}