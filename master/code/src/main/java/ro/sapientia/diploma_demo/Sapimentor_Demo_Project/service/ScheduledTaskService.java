package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.RoleRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class ScheduledTaskService {
    @Autowired
    private RoleRepository roleRepository;

    // 24 óra milliszekundumban
   // @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    // 5 perc milliszekundumban
    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void cleanInactiveRoles() {
        // Implementáld a szükséges logikát
        roleRepository.deleteInactiveRoles();
        System.out.println("Scheduled task is running...");
    }
}
