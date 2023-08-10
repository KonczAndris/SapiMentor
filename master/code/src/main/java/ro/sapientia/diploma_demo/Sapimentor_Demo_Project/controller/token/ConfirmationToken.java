package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.token;

import java.time.LocalDateTime;

public class ConfirmationToken {
    private long Id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;
}
