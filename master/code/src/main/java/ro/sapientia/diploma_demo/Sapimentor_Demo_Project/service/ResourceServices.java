package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Exams;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Resources;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ExamsRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ResourcesRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResourceServices {
    private final ResourcesRepository resourcesRepository;
    private final VirusTotalService virusTotalService;
    private final ExamsRepository examsRepository;

    public ResourceServices(ResourcesRepository resourcesRepository,
                            VirusTotalService virusTotalService,
                            ExamsRepository examsRepository) {
        this.resourcesRepository = resourcesRepository;
        this.virusTotalService = virusTotalService;
        this.examsRepository = examsRepository;
    }

    public List<Resources> getAllResources() {
        return resourcesRepository.findAll();
    }

    public Map<String, Integer> getLikeAndDislikeCounts(Long resourceId) {
        Resources resource = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));

        // Készíts egy Map objektumot a like és dislike értékekkel
        Map<String, Integer> likeAndDislikeCounts = new HashMap<>();
        likeAndDislikeCounts.put("like", resource.getLike());
        likeAndDislikeCounts.put("dislike", resource.getDislike());
        likeAndDislikeCounts.put("rowId", resource.getId().intValue());

        System.out.println("likeAndDislikeCounts: " + likeAndDislikeCounts);

        return likeAndDislikeCounts;
    }

    public void likeResource(Long resourceId) {
        Resources resource = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));

        // Noveljuk a like erteket
        resource.setLike(resource.getLike() + 1);
        resource.setDislike(resource.getDislike());
        // Elmentjuk az uj like erteket az adatbazisba
        resourcesRepository.save(resource);
    }

    public void dislikeResource(Long resourceId) {
        Resources resource = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));

        // Noveljuk a dislike erteket
        resource.setDislike(resource.getDislike() + 1);

        // Elmentjuk az uj dislike erteket az adatbazisba
        resourcesRepository.save(resource);
    }

    public void revokeLike(Long resourceId){
        Resources resource = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));

        // Csokkentjuk a like erteket
        resource.setLike(resource.getLike() - 1);

        // Elmentjuk az uj like erteket az adatbazisba
        resourcesRepository.save(resource);
    }

    public void revokeDislike(Long resourceId){
        Resources resource = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));

        // Csokkentjuk a dislike erteket
        resource.setDislike(resource.getDislike() - 1);

        System.out.println("Dislike: " + resource.getDislike());

        // Elmentjuk az uj dislike erteket az adatbazisba
        resourcesRepository.save(resource);
    }

    public void likeResourceAndRevokeDislike(Long resourceId){
        Resources resource = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));

        // Noveljuk a like erteket
        resource.setLike(resource.getLike() + 1);

        // Csokkentjuk a dislike erteket
        resource.setDislike(resource.getDislike() - 1);

        // Elmentjuk az uj like es dislike erteket az adatbazisba
        resourcesRepository.save(resource);
    }

    public void dislikeResourceAndRevokeLike(Long resourceId){
        Resources resource = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));

        // Noveljuk a dislike erteket
        resource.setDislike(resource.getDislike() + 1);

        // Csokkentjuk a like erteket
        resource.setLike(resource.getLike() - 1);

        // Elmentjuk az uj like es dislike erteket az adatbazisba
        resourcesRepository.save(resource);
    }

    // link letezesenek ellenorzese
    // Ez fontos ez majd kell a link ellenorzesehez
    public boolean isLinkAccessible(String url){
        try{
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            int responseCode = connection.getResponseCode();
            return (responseCode == 200);
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    // linkben talalhato-e karterek
    public boolean containsMaliciousContent(String url){
        String[] maliciousContent = {"<script>", "</script>", "<iframe>", "</iframe>"};
        for (String content : maliciousContent){
            if(url.contains(content)){
                // ez a jsoup a html kodot tisztitja meg a karterektol
                String cleanedUrl = Jsoup.clean(url, Whitelist.basic());
                if(cleanedUrl.contains(content)){
                    //System.out.println("Cleaned URL: " + cleanedUrl);
                    return true;
                }
            }
        }
        return false;
    }

    // API kulcs VirusTotal:
    // 7806c278ec2b1b0cf2a451eaf26d0a4e76d317569281c351640c9cee147dfb66
    // Uj metodus: URL-ellenorzes VirusTotal segitsegevel
    // itt a requestUrl-ben megadott URL-re kell POST kérést küldeni a VirusTotal API-nak
    public boolean isURLSafe(String url) {
        //System.out.println("URL to check: " + url);
        String response = virusTotalService.checkUrlSafety(url);
        System.out.println("VirusTotal response: " + response);
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = new JSONObject(response);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//        if (jsonObject.has("data")){
//            try {
//                JSONObject dataObject = jsonObject.getJSONObject("data");
//                if (dataObject.has("attributes")){
//                    JSONObject attributesObject = dataObject.getJSONObject("attributes");
//                    if(attributesObject.has("stats")){
//                        JSONObject statsObject = attributesObject.getJSONObject("stats");
//                        // Itt jelenítheted meg az "attributes"-bol a "stats" rész tartalmát
//                        System.out.println(statsObject.toString());
//                    }
//                }
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
//        }

        // ha a malicious erteke 0 es a harmless erteke nem 0, akkor a link biztonsagos
        // ha a malicious erteke nem 0, akkor a link karos vagyis nem biztonsagos
        // ha a harmless erteke 0, akkor nem sikerult a linket ellenorizni es ujra kell probalkozni
        if (response != null
                && response.contains("\"malicious\": 0")
                && !response.contains("\"harmless\": 0")) {
            return true;
        }
        return false;
    }

    //Ezzel tudod beallitani hogy mekkora legyen a maximalis meret amit feltolthet a felhasznalo
    private static final long MAX_IMAGE_SIZE = 2 * 1024 * 1024; // 2 MB
    //private static final long MAX_IMAGE_SIZE = 10 * 1024; // 10 KB
    //private static final long MAX_IMAGE_SIZE = 20 * 1024; // 20 KB
    //private static final long MAX_IMAGE_SIZE = 40 * 1024; // 40 KB

    // innen folytatni holnap 
    public String uploadExamImage(MultipartFile image,
                                  String name,
                                  String topic,
                                  String user_name){

        if (!image.isEmpty()){
            try{
//                System.out.println("Image size: " + image.getSize());
//                System.out.println("MAX_IMAGE_SIZE: " + MAX_IMAGE_SIZE);
//                System.out.println("Image name: " + name);
//                System.out.println("Image topic: " + topic);
//                System.out.println("Image user_name: " + user_name);
                if (image.getSize() > MAX_IMAGE_SIZE){
                    return "Too large";
                }

                byte[] originalImageBytes = image.getBytes();

                // itt hozom letre az Exams objektumot
                // es teszem bele a megadott adatokat
                Exams exam = new Exams();
                exam.setName(name);
                exam.setTopic_name(topic);
                exam.setUser_name(user_name);
                exam.setLike(0);
                exam.setDislike(0);

                // itt skalazom a kepet a megadott meretekre
                BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(originalImageBytes));

                int minDimension = Math.min(originalImage.getWidth(), originalImage.getHeight());
                int x = (originalImage.getWidth() - minDimension) / 2;
                int y = (originalImage.getHeight() - minDimension) / 2;

                BufferedImage croppedImage = originalImage.getSubimage(x, y, minDimension, minDimension);

                BufferedImage scaledImage = Thumbnails.of(croppedImage)
                        .size(400, 400)
                        .asBufferedImage();

                // be allitom a kimeneti fajltipust (pl. jpg)
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(scaledImage, "jpg", baos);
                byte[] scaledImageBytes = baos.toByteArray();

                // itt hozza adom a kepet az Exams objektumhoz
                exam.setExamImage(scaledImageBytes);
                examsRepository.save(exam);

            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
