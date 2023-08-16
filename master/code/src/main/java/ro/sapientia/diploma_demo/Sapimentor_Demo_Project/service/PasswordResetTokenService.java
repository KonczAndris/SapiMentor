package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.token.PasswordResetToken;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.PasswordResetTokenRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenService implements PasswordResetTokenServiceInterface{
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public String validatePasswordResetToken(String theToken) {
        Optional<PasswordResetToken> passwordResetToken = passwordResetTokenRepository.findByToken(theToken);
        if (passwordResetToken.isEmpty()){
            return "invalid";
        }
        Calendar calendar = Calendar.getInstance();
        if ((passwordResetToken.get().getExiprationTime().getTime()-calendar.getTime().getTime())<= 0){
            return "expired";
        }
        return "valid";
    }

    public Optional<User> findUserByPasswordResetToken(String theToken) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(theToken).get().getUser());
    }

    @Override
    public void resetPassword(User theUser, String newPassword) {
        theUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(theUser);
    }
    @Override
    public void createPasswordResetTokenForUser(User user, String passwordResetToken) {
        PasswordResetToken resetToken = new PasswordResetToken(passwordResetToken, user);
        passwordResetTokenRepository.save(resetToken);
    }
}
