package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity

public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private CrosConfig crosConfig;
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //.addFilterBefore(crosConfig.corsFilter(), CorsFilter.class)
                //.and()
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                //.and()
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
    }


    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

}
