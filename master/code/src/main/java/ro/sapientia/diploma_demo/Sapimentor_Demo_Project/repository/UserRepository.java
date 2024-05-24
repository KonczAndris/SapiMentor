package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.MyGroupProfileDetailDTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.UsersDetailsToAdminDTO;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Role;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

//megkerdezni, hogy ez miert kell

//@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query("SELECT u.id FROM User u WHERE u.email = :email")
    Long findIdByEmail(String email);

    @Query("SELECT new ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.UsersDetailsToAdminDTO( user.id, user.first_Name, user.last_Name, user.email, user.enabled, user.specialization, user.year, user.phoneNumber, user.profileImage, user.status, user.online_at) FROM User user ")
    List<UsersDetailsToAdminDTO> findAllUsersToAdmin();

    @Query("SELECT new ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.UsersDetailsToAdminDTO( user.id, user.first_Name, user.last_Name, user.email, user.enabled, user.specialization, user.year, user.phoneNumber, user.profileImage, user.status, user.online_at) FROM User user WHERE LOWER(user.first_Name) LIKE %:filter% OR LOWER(user.last_Name) LIKE %:filter%")
    List<UsersDetailsToAdminDTO> findFilteredUsersToAdmin(String filter);

    @Modifying
    @Query("UPDATE User u SET u.status = :status, u.online_at = :online_at WHERE u.id = :id")
    void updateUserStatusByIdToOnline(Long id, int status, LocalDateTime online_at);

    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.id = :id")
    void updateUserStatusByIdToOffline(Long id, int status);

    @Query("SELECT u.first_Name, u.last_Name FROM User u WHERE u.id = :id")
    String findNameById(Long id);

    @Query("SELECT u.roles FROM User u WHERE u.id = :userId")
    Collection<Role> findRolesByUserId(@Param("userId") Long userId);

    @Query("SELECT new ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.MyGroupProfileDetailDTO(u.first_Name, u.last_Name, u.id) FROM User u WHERE u.id != :userId")
    List<MyGroupProfileDetailDTO> findAllOtherUser(@Param("userId") Long userId);

    @Query("SELECT new ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.MyGroupProfileDetailDTO(u.first_Name, u.last_Name, u.id) FROM User u JOIN u.roles r WHERE r.name = 'MENTEE' AND u.id != :userId")
    List<MyGroupProfileDetailDTO> findAllMentees(@Param("userId") Long userId);

    @Query("SELECT new ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.dto.MyGroupProfileDetailDTO(u.first_Name, u.last_Name, u.id) FROM User u JOIN u.roles r WHERE r.name = 'MENTOR' AND u.id != :userId")
    List<MyGroupProfileDetailDTO> findAllMentors(@Param("userId") Long userId);

    @Query("SELECT u.profileImage, u.id FROM User u JOIN u.roles r WHERE r.name = 'MENTEE' AND u.id != :userId")
    List<Object[]> findallMenteeProfileImageById(@Param("userId") Long userId);

    @Query("SELECT u.profileImage, u.id FROM User u JOIN u.roles r WHERE r.name = 'MENTOR' AND u.id != :userId")
    List<Object[]> findallMentorProfileImageById(@Param("userId") Long userId);

    User findUserById(Long userId);

    List<User> findAllByIdIn(List<Long> userId);

    @Query("SELECT u.profileImage, u.id FROM User u WHERE u.id IN :userId")
    List<Object[]> findAllSelectedUserImages(List<Long> userId);

    @Query("SELECT u.profileImage, u.first_Name, u.last_Name FROM User u WHERE u.id = :userId")
    List<Object[]> findUserImageById(Long userId);

    @Query("SELECT u.profileImage, u.last_Name, u.first_Name FROM User u WHERE u.id = :userId")
    List<Object[]> findProfileImageById(Long userId);

    @Modifying
    @Query(value = "DELETE FROM user_role WHERE user_id = :userId", nativeQuery = true)
    void deleteUserRoleRecordsByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :userId")
    void deleteUserById(@Param("userId") Long userId);

}
