package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CrosConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("https://sapimentor.eu");
        config.addAllowedOrigin("https://www.sapimentor.eu");
        config.addAllowedHeader("*");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("GET");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
