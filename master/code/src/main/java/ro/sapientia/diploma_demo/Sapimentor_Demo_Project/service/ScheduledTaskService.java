package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ConfirmationTokenRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.RoleRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ScheduledTaskService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    // 24 óra milliszekundumban
   // @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    // 5 perc milliszekundumban
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void cleanInactiveRoles() {
        roleRepository.deleteInactiveRoles();
        System.out.println("Scheduled task is running...");
    }

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void deleteInactiveUsers() {
        List<Object[]> inactiveUsers = userRepository.findInactiveUsers();
        for (Object[] user : inactiveUsers) {
            Long userId = (Long) user[0];
            Boolean enabled = (Boolean) user[1];
            LocalDateTime registeredAt = (LocalDateTime) user[2];
            if(userId != null && enabled == false && registeredAt.isBefore(LocalDateTime.now())){
                System.out.println("Torlendo inaktiv felhasznalo: " + userId);
                userRepository.deleteUserRoleRecordsByUserId(userId);

                roleRepository.getAllRole_idByUserId(userId).forEach(role -> {
                    roleRepository.updateRolesStatusByIdCustom((Integer) role[0]);
                });
                confirmationTokenRepository.deleteByUserId(userId);
                userRepository.deleteUserById(userId);
                System.out.println("Inaktiv felhasznalo torolve: " + userId);
            }
        }
    }
}
