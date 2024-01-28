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

    // ez a metódus engedélyezi a websocketeket
    // a /user prefixen keresztül lehet elérni a websocketeket
    // a /app prefixen keresztül lehet küldeni üzeneteket a websocketeknek
    // a /user prefixen keresztül lehet küldeni üzeneteket a websocketeknek
    // a /user prefixen keresztül lehet feliratkozni a websocketekre
    // a /user prefixen keresztül lehet leiratkozni a websocketekről
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // Engedélyezzük a WebSocket-t a /user prefixen keresztül
        registry.enableSimpleBroker("/topic");
        // Az üzenetek küldésére használt prefix
        registry.setApplicationDestinationPrefixes("/app");
        // Felhasználói célú prefix a WebSocket endpointeken keresztül
        registry.setUserDestinationPrefix(("/user"));
    }

    // ez a metódus regisztrálja a websocketeket
    // a /ws endpointen keresztül lehet elérni a websocketeket
    // a withSockJS() metódus engedélyezi a fallback lehetőséget
    // a fallback lehetőség akkor használatos, ha a websocketek nem elérhetőek
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // Regisztráljuk a /ws endpointet a WebSocketekhez
        registry.addEndpoint("/ws").withSockJS();
    }

    // ez a metódus beállítja a JSON konvertert
    // a JSON konverter a websocketeknél a JSON formátumú üzeneteket alakítja át
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

        // a false érték azt jelenti, hogy nem használunk más konvertert
        return false;
    }

}
