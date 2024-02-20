package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Role;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Modifying
    @Query("UPDATE Role r SET r.status = 0 WHERE r.id = :roleId")
    void updateRolesStatusByIdCustom(@Param("roleId") Integer roleId);

    @Modifying
    @Query("DELETE FROM Role r WHERE r.status = 0")
    void deleteInactiveRoles();

    @Query(value = "SELECT ur.role_id FROM user_role ur WHERE user_id = :userId", nativeQuery = true)
    List<Object[]> getAllRole_idByUserId(@Param("userId") Long userId);

}
