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

    //@Value("${virustotal.api.key}")
    private final String apiKey = "7806c278ec2b1b0cf2a451eaf26d0a4e76d317569281c351640c9cee147dfb66";
    //private final String virusTotalApiUrl = "https://www.virustotal.com/api/v2/url/report?apikey={apiKey}&resource={url}";
    private final RestTemplate restTemplate;

    public VirusTotalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // A VirusTotal API v3-at hasznalom mert a v2 mar nem tamogatott.

    public String checkUrlSafety(String url) {
        try {
            // itt a requestUrl-ben megadott URL-re kell POST kérést küldeni a VirusTotal API-nak
            // A requestUrl-ben megadott URL-t a "url" paraméterben kell megadni.
            // ez a resz azt csinalja, hogy a requestUrl-ben megadott URL-re POST kerest kuldd a VirusTotal API-nak
            String requestUrl = "https://www.virustotal.com/api/v3/urls";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(requestUrl))
                    .header("accept", "application/json")
                    .header("x-apikey", apiKey)
                    .header("content-type", "application/x-www-form-urlencoded")
                    .method("POST", HttpRequest.BodyPublishers.ofString("url=" + url))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            //System.out.println("Response body: " + response.body());

            // itt a response body-bol kell kinyerni az "analysisId"-t
            String analysisId = extractAnalysisId(response.body());

            // ez a parhuzamos reszhez kell
            // CompletableFuture<String> analysisStatusFuture = getAnalysisStatus(analysisId);

            String analysisStatus = getAnalysisStatus(analysisId);
            if(!"completed".equals(analysisStatus)){
                while (true){
                    analysisStatus = getAnalysisStatus(analysisId);
                    if ("completed".equals(analysisStatus)) {
                        break;
                    }
                    // ha az analysis status meg nem completed, akkor varunk egy kicsit
                    TimeUnit.SECONDS.sleep(3);
                }
            }
//            while (!"completed".equals(analysisStatus)) {
//                // itt lekerdezzuk az analysis status-t
//                analysisStatus = getAnalysisStatus(analysisId);
//                // ha az analysis status meg nem completed, akkor varunk 3 masodpercet es ujra lekerdezzuk az analysis status-t
//                TimeUnit.SECONDS.sleep(5);
//                //Thread.sleep(1000);
//            }
//            System.out.println("Analysis status2: " + analysisStatusFuture.get());
//            while (true) {
//                String analysisStatus = analysisStatusFuture.get(); // Várakozás a párhuzamos eredményre
//                System.out.println("Analysis status3: " + analysisStatus);
//                if ("completed".equals(analysisStatus)) {
//                    break;
//                }
//                // ha az analysis status még nem completed, akkor várunk egy kicsit
//                TimeUnit.SECONDS.sleep(3);
//            }

            // itt a response body-bol kinyert "analysisId"-t kell hasznalni a safety ellenorzeshez
            //String safetyResult = checkSafety(analysisId);

            return checkSafety(analysisId);
        } catch (Exception e) {
            e.printStackTrace();
            return "Hiba történt az URL ellenőrzése közben";
        }
    }

        // itt a parhuzamos tipusu keres van megvalositva
//    private CompletableFuture<String> getAnalysisStatus(String analysisId) throws JSONException {
//        String requestUrl = "https://www.virustotal.com/api/v3/analyses/" + analysisId;
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(requestUrl))
//                .header("accept", "application/json")
//                .header("x-apikey", apiKey)
//                .method("GET", HttpRequest.BodyPublishers.noBody())
//                .build();
//
//        CompletableFuture<HttpResponse<String>> responseFuture = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
//
//        return responseFuture.handle((response, throwable) -> {
//            if (throwable != null) {
//                throwable.printStackTrace();
//                return "error";
//            } else {
//                try {
//                    JSONObject jsonObject = new JSONObject(response.body());
//
//                    JSONObject dataObject = jsonObject.getJSONObject("data");
//                    System.out.println("Analysis status1: " + dataObject.getString("attributes"));
//                    return dataObject.getJSONObject("attributes").getString("status");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    return "error";
//                }
//            }
//        });
//    }



    //itt a response body-bol kinyerem az "analysisId"-t
    private String extractAnalysisId(String responseBody) {
        try {
            // Az adott JSON választ egy JSON objektumként kell feldolgozni.
            JSONObject jsonObject = new JSONObject(responseBody);

            // A "data" objektumból az "id" kulcs értéke a keresett "analysisId".
            JSONObject dataObject = jsonObject.getJSONObject("data");
            //String analysisId = dataObject.getString("id");

            return dataObject.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
            return null; // Vagy valamilyen hiba kezelése, például null visszaadása
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
            //System.out.println("Safety Response body: " + response.body());

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
            System.out.println("Analysis status: " + dataObject.getString("attributes"));
            return dataObject.getJSONObject("attributes").getString("status");
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

}



//package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;
//
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class VirusTotalService {
//
//    //@Value("${virustotal.api.key}")
//    private final String apiKey = "7806c278ec2b1b0cf2a451eaf26d0a4e76d317569281c351640c9cee147dfb66";
//    private final String virusTotalApiUrl = "https://www.virustotal.com/vtapi/v2/url/report?apikey={apiKey}&resource={url}";
//    private final RestTemplate restTemplate;
//
//    public VirusTotalService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public String checkUrlSafety(String url) {
//        String urlToCheck = url.replace("http://", "").replace("https://", "");
//        String apiUrl = virusTotalApiUrl.replace("{apiKey}", apiKey).replace("{url}", urlToCheck);
//        String response = restTemplate.getForObject(apiUrl, String.class);
//        return response;
//    }
//}
