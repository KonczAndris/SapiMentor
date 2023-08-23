package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;


import lombok.RequiredArgsConstructor;
//import lombok.experimental.var;
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
    //private static final long MAX_IMAGE_SIZE = 2 * 1024 * 1024; // 2 MB
    private static final long MAX_IMAGE_SIZE = 10 * 1024; // 10 KB

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
//        System.out.println("calendar time: " + calendar.getTime().getMinutes());
//        System.out.println("token time: " + token.getExpirationTime().getMinutes());
//        System.out.println("time difference: " + (token.getExpirationTime().getTime()- calendar.getTime().getTime()));
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

    public void uploadProfileImage(String email, MultipartFile image) {
        User user = userRepository.findByEmail(email);
        if (user != null && !image.isEmpty() && image.getSize() <= MAX_IMAGE_SIZE) {
            try {
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
            //catch (IllegalArgumentException e) {
//                // Beállítod az üzenetet, hogy túl nagy a kép mérete
//                model.addAttribute("errorMessage", "The uploaded image is too large. Please choose a smaller image.");
//            }
        }
    }


//    //bejelekezesnel kell
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByEmail(username);
//        if(user == null || !user.isEnabled()) {
//            throw new UsernameNotFoundException("Invalid username or password.");
//        }
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
//    }
//
//    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
//        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
//    }



}
