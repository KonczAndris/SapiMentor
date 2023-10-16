package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Resources;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Role;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Topic;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.User;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.ResourcesRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.ResourceServices;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.TopicService;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.VirusTotalService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/resources")
@Controller
public class ResourcesController {
    private final UserRepository userRepository;
    private final TopicService topicService;
    private final ResourcesRepository resourcesRepository;

    private final ResourceServices resourceServices;
    private final SseController sseController;
    private final VirusTotalService virusTotalService;

    @Autowired
    public ResourcesController(UserRepository userRepository,
                               TopicService topicService,
                               ResourcesRepository resourcesRepository,
                               ResourceServices resourceServices,
                               SseController sseController,
                               VirusTotalService virusTotalService) {
        this.userRepository = userRepository;
        this.topicService = topicService;
        this.resourcesRepository = resourcesRepository;
        this.resourceServices = resourceServices;
        this.sseController = sseController;
        this.virusTotalService = virusTotalService;
    }

    private void showUserRolesToDisplayResources(Model model, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        //System.out.println("User: " + user.getEmail());

        List<String> rolesToDisplayResources = new ArrayList<>();
        boolean hasOtherRole = false;

        for (Role role : user.getRoles()){
            if(!role.getName().equals("USER")){
                rolesToDisplayResources.add(role.getName());
                hasOtherRole = true;
            }
        }

        if(!hasOtherRole){
            rolesToDisplayResources.add("USER");
        }

        String rolesAsString = String.join(",", rolesToDisplayResources);
        //System.out.println("Roles: " + rolesAsString);
        model.addAttribute("userRolesToDisplayResources", rolesAsString);
    }

    private void showTopicsToDisplayResources(Model model, Principal principal){
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        // Itt lekérem a témákat a service segítségével
        List<Topic> topics = topicService.getAllTopics();
        //System.out.println("Topics: " + topics);
        model.addAttribute("topics", topics);
    }

    @GetMapping("")
    public String showResources(Model model, Principal principal) {
        showUserRolesToDisplayResources(model, principal);
        showTopicsToDisplayResources(model, principal);
        List<Resources> resources = resourceServices.getAllResources();
        //System.out.println("Resources: " + resources);
        model.addAttribute("resourcesData", resources);

        return "resources";
    }
    @GetMapping("/examExamples")
    public String showExamExamples(Model model, Principal principal) {
        showUserRolesToDisplayResources(model, principal);
        showTopicsToDisplayResources(model, principal);
        return "examExamples";
    }
    @GetMapping("/diplomaTheses")
    public String showDiplomaTheses(Model model, Principal principal) {
        showUserRolesToDisplayResources(model, principal);
        showTopicsToDisplayResources(model, principal);
        return "diplomaTheses";
    }

    @PostMapping("/uploadResources")
    public ResponseEntity<String> uploadResources(String resourcesUploadDataItems,
                                                  Principal principal){
        //ez a objectMapper a json stringet alakitja at objektumokka
        ObjectMapper objectMapper = new ObjectMapper();
        //System.out.println("Resources upload data items: " + resourcesUploadDataItems);

        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        //System.out.println("User_Name: " + user.getFirst_Name());
        try{
            // JSON string deszerializálása objektumokká
            Resources[] resources_dataItems = objectMapper.readValue(resourcesUploadDataItems, Resources[].class);
            //System.out.println("Resources data items: " + resources_dataItems);

            for (Resources resourcesData : resources_dataItems){
                String name = resourcesData.getName();
                String link = resourcesData.getLink();
                String topic_name = resourcesData.getTopic_name();
                String user_name = user.getFirst_Name() + " " + user.getLast_Name();
                Integer like = 0;
                Integer dislike = 0;
//                System.out.println("IsLinkAccessible: " + isLinkAccessible(link));
//                System.out.println("ContainsMaliciousContent: " + containsMaliciousContent(link));
                //System.out.println("IsURLSafe: " + isURLSafe(link));

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

                            // Resources entitas elmentese az adatbazisba
                             resourcesRepository.save(resources);
                        } else {
                            // link biztonsagos-e vagy karos
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NotSafe");
                        }
                    } else {
                        // linkben talalhato karterek
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("MaliciousContent");
                    }
                } else {
                    // link ervenytelen
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("InvalidLink");
                }
            }
            return ResponseEntity.ok("Success");
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }


    // link letezesenek ellenorzese
    // Ez fontos ez majd kell a link ellenorzesehez
    private boolean isLinkAccessible(String url){
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
    private boolean containsMaliciousContent(String url){
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
    private boolean isURLSafe(String url) {
        //System.out.println("URL to check: " + url);
        String response = virusTotalService.checkUrlSafety(url);
        //System.out.println("VirusTotal response: " + response);
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

    // idaig kell haladni

    @PostMapping("/like")
    public ResponseEntity<String> likeResource(@RequestParam Long resourceId) {
        //System.out.println("Resource ID: " + resourceId);
        resourceServices.likeResource(resourceId);
        return ResponseEntity.ok("Liked resource with ID: " + resourceId);
    }

    @PostMapping("/dislike")
    public ResponseEntity<String> dislikeResource(@RequestParam Long resourceId) {
        resourceServices.dislikeResource(resourceId);
        return ResponseEntity.ok("Disliked resource with ID: " + resourceId);
    }

    @PostMapping("/revokelike")
    public ResponseEntity<String> RevokeLike(@RequestParam Long resourceId) {
        resourceServices.revokeLike(resourceId);
        return ResponseEntity.ok("Revoke Like resource with ID: " + resourceId);
    }

    @PostMapping("/revokedislike")
    public ResponseEntity<String> RevokeDislike(@RequestParam Long resourceId) {
        resourceServices.revokeDislike(resourceId);
        return ResponseEntity.ok("Revoke Dislike resource with ID: " + resourceId);
    }

}
