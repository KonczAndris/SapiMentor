package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;

@Service
public class VirusTotalService {

    private final String apiKey = "7806c278ec2b1b0cf2a451eaf26d0a4e76d317569281c351640c9cee147dfb66";
    private final RestTemplate restTemplate;

    public VirusTotalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String checkUrlSafety(String url) {
        try {
            String requestUrl = "https://www.virustotal.com/api/v3/urls";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .header("accept", "application/json")
                    .header("x-apikey", apiKey)
                    .header("content-type", "application/x-www-form-urlencoded")
                    .method("POST", HttpRequest.BodyPublishers.ofString("url=" + url))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            // itt a response body-bol kell kinyerni az "analysisId"-t
            String analysisId = extractAnalysisId(response.body());

            String analysisStatus = getAnalysisStatus(analysisId);
            if(!"completed".equals(analysisStatus)){
                while (true){
                    analysisStatus = getAnalysisStatus(analysisId);
                    if ("completed".equals(analysisStatus)) {
                        break;
                    }
                    TimeUnit.SECONDS.sleep(3);
                }
            }

            return checkSafety(analysisId);
        } catch (Exception e) {
            e.printStackTrace();
            return "Hiba történt az URL ellenőrzése közben";
        }
    }

    //itt a response body-bol kinyerem az "analysisId"-t
    private String extractAnalysisId(String responseBody) {
        try {
            // Az adott JSON választ egy JSON objektumként kell feldolgozni.
            JSONObject jsonObject = new JSONObject(responseBody);

            // A "data" objektumból az "id" kulcs értéke a keresett "analysisId".
            JSONObject dataObject = jsonObject.getJSONObject("data");
            return dataObject.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    // itt a response body-bol kinyert "analysisId"-t kell hasznalni a safety ellenorzeshez
    // a "checkSafety" fuggveny visszaterese egy JSON stringgel, amiben benne van az URL biztonsagi erteke
    private String checkSafety(String analysisId) {
        try {
            String requestUrl = "https://www.virustotal.com/api/v3/analyses/" + analysisId;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .header("accept", "application/json")
                    .header("x-apikey", apiKey)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return "Hiba történt az URL biztonság ellenőrzése közben";
        }
    }

    private String getAnalysisStatus(String analysisId) {
        try {
            String requestUrl = "https://www.virustotal.com/api/v3/analyses/" + analysisId;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .header("accept", "application/json")
                    .header("x-apikey", apiKey)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonObject = new JSONObject(response.body());

            JSONObject dataObject = jsonObject.getJSONObject("data");
            return dataObject.getJSONObject("attributes").getString("status");
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

}