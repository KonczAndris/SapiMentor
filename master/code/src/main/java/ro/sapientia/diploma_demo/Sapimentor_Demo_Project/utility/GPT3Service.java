package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class GPT3Service {
    @Value("${openai.apiKey}")
    private String apiKey;

    private final WebClient webClient;

    public GPT3Service(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1").build();
    }

    public String getKeywordsFromAbstractWithGPT3(String userMessage) {
        //System.out.println("User message: " + userMessage);

        // Elokeszitjuk a lekerdezesi torzset
        String requestBodyToGPT3 = String.format(
                "{\"model\":\"gpt-3.5-turbo\",\"messages\":[{\"role\":\"system\",\"content\":\"You are a helpful assistant.\"},{\"role\":\"user\",\"content\":\"%s\"}]}",
                userMessage
        );

        // Itt hivjuk meg a GPT-3 API-t
        try {
            String gpt3Response = webClient.post()
                    .uri("/chat/completions")
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .body(BodyInserters.fromValue(requestBodyToGPT3))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            //System.out.println("API response: " + response);
            return gpt3Response;
        } catch (WebClientResponseException e) {
            System.err.println("API error response: " + e.getResponseBodyAsString());
            e.printStackTrace();
            return "Error GPT-3 API";
        }
    }
}
