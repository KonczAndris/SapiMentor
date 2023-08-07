package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
//@EnableWebSecurity
public class WebSecurityConfiguration {

    //ez a password encoder, amit a UserServiceImpl-ben hasznalunk (ezzel kódoljuk a jelszavakat)
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
