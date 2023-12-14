package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.springframework.stereotype.Service;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class MyGroupService {

    private final UserRepository userRepository;

    public MyGroupService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Object[]> getAllProfileImageById(Long userId) {
        return userRepository.findallProfileImageById(userId);
    }

}
