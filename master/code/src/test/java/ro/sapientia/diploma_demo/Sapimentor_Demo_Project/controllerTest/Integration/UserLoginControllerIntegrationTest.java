package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controllerTest.Integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller.token.ConfirmationToken;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.PasswordResetTokenServiceInterface;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserLoginControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PasswordResetTokenServiceInterface passwordResetTokenServiceInterface;

    @Test
    public void testLogin() throws Exception {
        // Szimulált HTTP kérés küldése
        mockMvc.perform(post("/login").param("username", "szotyori.csongor@student.ms.sapientia.ro")
                .param("password", "proba123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }


    @Test
    // itt azt teszteljuk hogy a /forgot-password endpoint megfeleloen mukodik-e
    // a is3xxRedirection() azt teszteli hogy a keres sikeres volt-e
    // a redirectedUrl("/forgotPassword?success") azt teszteli hogy a keres sikeres volt-e
    public void testProcessForgotPassword() throws Exception {
        // Szimulált HTTP kérés küldése
        mockMvc.perform(post("/forgot-password").param("email", "szotyori.csongor@student.ms.sapientia.ro"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forgotPassword?success"))
                .andDo(print()); ;
    }

    @Test
    public void testProcessResetPassword() throws Exception {
        String token = "5b964069-9279-4677-b4e8-8d2347334bd6";
        String password = "proba123";

        // Mock token létrehozása
        ConfirmationToken mockToken = new ConfirmationToken();
        mockToken.setToken(token);


        // Teszt: HTTP POST kérés szimulálása a resetPassword végponton
        mockMvc.perform(MockMvcRequestBuilders.post("/reset-password")
                        .param("token", token)
                        .param("password", password))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login?reset_success"));
    }






}
