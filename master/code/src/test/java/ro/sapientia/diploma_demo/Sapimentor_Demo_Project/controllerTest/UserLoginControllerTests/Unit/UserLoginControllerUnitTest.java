package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controllerTest.UserLoginControllerTests.Unit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.UserLoginController;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UserLoginControllerUnitTest {
    @InjectMocks
    private UserLoginController userLoginController;

    @Test
    public void TestShowLoginPage() {
        String result = userLoginController.login();
        assertEquals("login", result);
    }

    @Test
    public void TestShowForgotPasswordPage() {
        String result = userLoginController.showForgotPasswordForm();
        assertEquals("forgotPassword", result);
    }

    @Test
    public void TestShowNewPasswordPage() {
        String token = "exampleToken";

        Model model = new ExtendedModelMap();
        String result = userLoginController.showNewPasswordForm(token, model);
        assertEquals("newPassword", result);
        assertEquals(token, model.getAttribute("token"));
        System.out.println(model);
    }





}
