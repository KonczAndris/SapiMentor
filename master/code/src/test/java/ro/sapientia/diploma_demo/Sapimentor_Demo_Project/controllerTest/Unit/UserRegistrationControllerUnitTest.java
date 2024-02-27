package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controllerTest.Unit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.UserRegistrationController;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UserRegistrationControllerUnitTest {
    @InjectMocks
    private UserRegistrationController userRegistrationController;

    @Test
    public void TestShowRegistrationPage() {
        String result = userRegistrationController.showRegistrationForm();
        assertEquals("register", result);
    }
}
