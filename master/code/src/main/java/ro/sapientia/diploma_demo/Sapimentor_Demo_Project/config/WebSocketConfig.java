package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // Engedélyezzük a WebSocket-t a /user prefixen keresztül
        registry.enableSimpleBroker("/user");
        // Az üzenetek küldésére használt prefix
        registry.setApplicationDestinationPrefixes("/app");
        // Felhasználói célú prefix a WebSocket endpointeken keresztül
        registry.setUserDestinationPrefix(("/user"));
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // Regisztráljuk a /ws endpointet a WebSocketekhez
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:8080", "https://www.sapimentor.eu", "https://sapimentor.eu"
                        , "https://www.sapimentor.onrender.com")
                .withSockJS();
    }


    // hogy a websocketeknél JSON formátumú üzeneteket tudjunk küldeni
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {

        // Beállítjuk a JSON konvertert
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(APPLICATION_JSON);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);
        messageConverters.add(converter);

        // nem használunk más konvertert
        return false;
    }

}
