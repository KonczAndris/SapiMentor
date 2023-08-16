package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;

import java.util.Optional;

public interface PasswordResetTokenServiceInterface {
    String validatePasswordResetToken(String theToken);

    Optional<User> findUserByPasswordResetToken(String theToken);

    void resetPassword(User theUser, String password);

    void createPasswordResetTokenForUser(User user, String passwordResetToken);
}
