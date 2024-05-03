package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Resources;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ExamsRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ResourcesRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserResourceLikeDislikeRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
@Transactional
public class ResourceServices {
    private final ResourcesRepository resourcesRepository;
    private final VirusTotalService virusTotalService;
    private final ExamsRepository examsRepository;
    private final UserResourceLikeDislikeRepository userResourceLikeDislikeRepository;

    public ResourceServices(ResourcesRepository resourcesRepository,
                            VirusTotalService virusTotalService,
                            ExamsRepository examsRepository,
                            UserResourceLikeDislikeRepository userResourceLikeDislikeRepository) {
        this.resourcesRepository = resourcesRepository;
        this.virusTotalService = virusTotalService;
        this.examsRepository = examsRepository;
        this.userResourceLikeDislikeRepository = userResourceLikeDislikeRepository;
    }

    // van teszt irva ra
    @Cacheable("getAllResources")
    public List<Resources> getAllResources() {
        return resourcesRepository.findAll();
    }

    @Cacheable("getAllResourcesByFilter")
    public List<Resources> getAllResourcesByFilter(Model model,
                                        String filter,
                                        String[] selectedValues) {
        List<Resources> filteredResources = new ArrayList<>();
        String filterValue = filter;
        String[] selectedValuesArray = selectedValues;
//        System.out.println("Igen1:" + filterValue);
//        System.out.println("Igen2:" + Arrays.toString(selectedValuesArray));
        filteredResources = resourcesRepository.findAllByNameAndTopicName(filterValue, selectedValuesArray);
//        for(Resources resourcesList : filteredResources) {
//            System.out.println("Igen3:" + resourcesList.getName()
//            + " , " + resourcesList.getTopic_name());
//        }
        return filteredResources;
    }

    // van teszt irva ra
    @Cacheable("getLikeAndDislikeCounts")
    public Map<String, Integer> getLikeAndDislikeCounts(Long resourceId) {
        Resources resource = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));

        // Készíts egy Map objektumot a like és dislike értékekkel
        Map<String, Integer> likeAndDislikeCounts = new HashMap<>();
        likeAndDislikeCounts.put("like", resource.getLike());
        likeAndDislikeCounts.put("dislike", resource.getDislike());
        likeAndDislikeCounts.put("rowId", resource.getId().intValue());

        //System.out.println("likeAndDislikeCounts: " + likeAndDislikeCounts);

        return likeAndDislikeCounts;
    }

    // van teszt irva ra
    public void likeResource(Long resourceId) {
        Resources resource = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));

        // Noveljuk a like erteket
        resource.setLike(resource.getLike() + 1);
        resource.setDislike(resource.getDislike());
        // Elmentjuk az uj like erteket az adatbazisba
        resourcesRepository.save(resource);
    }

    // van teszt irva ra
    public void dislikeResource(Long resourceId) {
        Resources resource = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));

        // Noveljuk a dislike erteket
        resource.setDislike(resource.getDislike() + 1);

        // Elmentjuk az uj dislike erteket az adatbazisba
        resourcesRepository.save(resource);
    }


    // van ra irva teszt
    public void revokeLike(Long resourceId){
        Resources resource = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));

        // Csokkentjuk a like erteket
        resource.setLike(resource.getLike() - 1);

        // Elmentjuk az uj like erteket az adatbazisba
        resourcesRepository.save(resource);
    }

    // van ra irva teszt
    public void revokeDislike(Long resourceId){
        Resources resource = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + resourceId));

        // Csokkentjuk a dislike erteket
        resource.setDislike(resource.getDislike() - 1);

        //System.out.println("Dislike: " + resource.getDislike());

        // Elmentjuk az uj dislike erteket az adatbazisba
        resourcesRepository.save(resource);
    }

    // van ra irva teszt
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

    // van ra irva teszt
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

    // van ra irva teszt
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

    // van ra irva teszt
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



    // van ra irva teszt

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

    public String deleteResources(Long linkId) {
        try {
            userResourceLikeDislikeRepository.deleteByResourceId(linkId);
            resourcesRepository.deleteById(linkId);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
        return null;
    }

    public String modifyResources(String name,
                                  String topic,
                                  String user_name,
                                  Long resourceId) {
        try {
            Resources resource = resourcesRepository.findById(resourceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Resource not found!"));
            if (resource != null) {
                resource.setName(name);
                resource.setTopic_name(topic);
                resource.setUser_name(user_name);

                resourcesRepository.save(resource);
            } else {
                return "Not found!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
        return null;
    }

    // van ra irva teszt
    public String processAndSaveResources(Resources[] resourcesDataItems,
                                          String fullUserName,
                                          Long user_id) {
        try {
            for (Resources resourcesData : resourcesDataItems){
                String name = resourcesData.getName();
                String link = resourcesData.getLink();
                String topic_name = resourcesData.getTopic_name();
                String user_name = fullUserName;
                Integer like = 0;
                Integer dislike = 0;

                // link ervenyessegenek ellenorzese
                if (isLinkAccessible(link)){
                    // linkben talalhato-e karterek
                    if(!containsMaliciousContent(link)){
                        if(isURLSafe(link)){
                            // Adatok elmentese a Resources entitasba
                            Resources resources = new Resources();
                            resources.setName(name);
                            resources.setLink(link);
                            resources.setTopic_name(topic_name);
                            resources.setUser_name(user_name);
                            resources.setLike(like);
                            resources.setDislike(dislike);
                            resources.setUser_id(user_id);
                            // Resources entitas elmentese az adatbazisba
                             resourcesRepository.save(resources);
                        } else {
                            // link biztonsagos-e vagy karos
                            return "NotSafe";
                        }
                    } else {
                        // linkben talalhato karterek
                        return "MaliciousContent";
                    }
                } else {
                    // link ervenytelen
                    return "InvalidLink";
                }
            }
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

}
