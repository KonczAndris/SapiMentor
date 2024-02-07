package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility.UserStatusModificator;

import java.security.Principal;

@Controller
public class UserStatusController {

    @Autowired
    private UserStatusModificator userStatusModificator;

    @PostMapping("/myGroup/favorites/updateUserStatusToOnline")
    public ResponseEntity<String> updateUserStatusToOnline(Principal principal) {
        try {
            String email = principal.getName();
            int status = 1;
            userStatusModificator.updateUserStatusToOnlineByEmail(email, status);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during saving favourite!");
        }
    }

    @PostMapping("/myGroup/favorites/updateUserStatusToOffline")
    public ResponseEntity<String> updateUserStatusToOffline(Principal principal) {
        try {
            System.out.println("updateUserStatusToOffline");
            String email = principal.getName();
            int status = 0;
            userStatusModificator.updateUserStatusToOnlineByEmail(email, status);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during saving favourite!");
        }
    }

}
