package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.token.ConfirmationToken;


public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    ConfirmationToken findByToken(String token);

    @Modifying
    @Query("DELETE FROM ConfirmationToken c WHERE c.user.id = :userId")
    void deleteByUserId(Long userId);
}
