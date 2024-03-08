package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.token.PasswordResetToken;

import java.util.Optional;


public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String theToken);

    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.user.id = :userId")
    void deleteByUserId(Long userId);
}
