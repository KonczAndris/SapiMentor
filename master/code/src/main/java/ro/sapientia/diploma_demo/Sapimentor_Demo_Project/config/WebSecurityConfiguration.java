package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {


//    @Autowired
//    private UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public MyCustomSessionInformationExpiredStrategy customSessionInformationExpiredStrategy() {
//        return new MyCustomSessionInformationExpiredStrategy();
//    }
//
//    @Bean
//    public HttpSessionEventPublisher httpSessionEventPublisher() {
//        return new HttpSessionEventPublisher();
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors((cors) -> cors
                .configurationSource(corsConfigurationSource())
        )
                //.and()
                .csrf()
                //.disable()
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()
                .and()
                // idaig a session kezeles
                .authorizeRequests()
                    .antMatchers("/register/**", "/js/**", "/css/**", "/img/**", "/fonts/**", "/forgotPassword/**", "/pdf/**", "/gif/**")
                    .permitAll()
                    .antMatchers("/")
                    .authenticated()
                    .antMatchers("/profile/**")
                    .authenticated()
                    .antMatchers("/saveProfileTopics")
                    .authenticated()
                    .antMatchers("/deleteTopicAndSkills")
                    .authenticated()
                    .antMatchers("/resources/**")
                    .authenticated()
                    .antMatchers("/settings/**")
                    .authenticated()
                    .antMatchers("/topics/**")
                    .authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .failureUrl("/login?error")
                    .permitAll()
                .and()
                .logout()
                //.logoutUrl("/logout")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout")
                    .permitAll();
                //.and()
                //innentol a session kezeles
//                .sessionManagement()
//                    .maximumSessions(1)
//                    .maxSessionsPreventsLogin(false)
//                    .expiredUrl("/login?expiredSession")
//                    .and()
//                    .invalidSessionUrl("/login");

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://www.sapimentor.eu","https://sapimentor.eu","http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

}
