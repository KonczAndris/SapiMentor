package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Rating;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.DiplomaServices;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.ExamServices;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.ResourceServices;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/sse")
public class SseController {
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ResourceServices resourceServices;
    private final ExamServices examServices;
    private final DiplomaServices diplomaServices;

    private final UserRepository userRepository;

    public SseController(ResourceServices resourceServices,
                         ExamServices examServices,
                         DiplomaServices diplomaServices,
                         UserRepository userRepository) {
        this.resourceServices = resourceServices;
        this.examServices = examServices;
        this.diplomaServices = diplomaServices;
        this.userRepository = userRepository;
    }

    // SSE endpoint
    // itt a felhasznalo feliratkozik a SSE-re
    // a feliratkozas utan a kliens kap egy SSE objektumot
    // amit a kliens eltarol
    // a kliensnek a SSE objektumot kell hasznalnia a kliens es a szerver kozotti kommunikaciohoz
    @GetMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe() {
        // SSE objektum letrehozasa
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try {
            // SSE objektum elkuldes
            sseEmitter.send(SseEmitter.event().name("INIT"));
        } catch (IOException e){
            e.printStackTrace();
        }
        // SSE objektum lezarasa
        sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));

        emitters.add(sseEmitter);
        return sseEmitter;
    }
    //exeption handler vagy global exception handler

    // SSE endpoint (Links oldalon kapja meg a Like es Dislike erteket)
    // itt a szerver elkuldi a kliensnek az uzenetet
    // a kliensnek a SSE objektumot kell hasznalnia a kliens es a szerver kozotti kommunikaciohoz
    @PostMapping("/sendLikeOrDislike")
    public ResponseEntity<String> sendSseMessage(@RequestBody Map<String, String> data) {
        //System.out.println("SSE message sent(data): " + data);
        String message = data.get("message");
        //System.out.println("SSE message sent1(message): " + message);
        if (message == null || message.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid message");
        }
        //System.out.println("SSE message sent2(message): " + message);

        //A message-t elvalasztjuk a ":" karakterrel
        String[] messageParts = message.split(":");

        if (messageParts.length == 2) {
            //Action kinyerese a message-bol
            String action = messageParts[0];
            //System.out.println("SSE message sent(action): " + action);

            //Resource ID kinyerese a message-bol
            Long resourceId = Long.parseLong(messageParts[1]);
            //System.out.println("SSE message sent(resourceId): " + resourceId);

            Map<String, Integer> likeAndDislikeCounts = resourceServices.getLikeAndDislikeCounts(resourceId);
            //System.out.println("SSE message sent(likeAndDislikeCounts): " + likeAndDislikeCounts);

            for (SseEmitter emitter : emitters) {
                try {
                    //A kliensnek elkuldom a like es dislike szamot
                    emitter.send(SseEmitter.event().name("LikeOrDislike").data(likeAndDislikeCounts));
                } catch (IOException e) {
                    emitters.remove(emitter);
                }
            }

        } else {
            System.err.println("Invalid message: " + message);
            return ResponseEntity.badRequest().body("Invalid message");
        }

        return ResponseEntity.ok("SSE message sent");
    }

    @PostMapping("/sendLikeOrDislikeForExam")
    public ResponseEntity<String> sendSseMessageForExam(@RequestBody Map<String, String> data) {
        //System.out.println("SSE message sent(data): " + data);
        String message = data.get("message");
        //System.out.println("SSE message sent1(message): " + message);
        if (message == null || message.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid message");
        }
        //System.out.println("SSE message sent2(message): " + message);

        //A message-t elvalasztjuk a ":" karakterrel
        String[] messageParts = message.split(":");

        if (messageParts.length == 2) {
            //Action kinyerese a message-bol
            String action = messageParts[0];
            //System.out.println("SSE message sent(action): " + action);

            //Exam ID kinyerese a message-bol
            Long examId = Long.parseLong(messageParts[1]);
            //System.out.println("SSE message sent(examId): " + examId);

            Map<String, Integer> likeAndDislikeCounts = examServices.getLikeAndDislikeCounts(examId);
            //System.out.println("SSE message sent(likeAndDislikeCounts): " + likeAndDislikeCounts);

            // itt az emitters listaban levo osszes SSE objektumot vegig kell iteralni
            // es mindegyik SSE objektumot elkuldeni a kliensnek
            // az emitterben a kovetkezo adatok vannak:
            // - SSE objektum
            // - SSE objektum lezarasa
            // - SSE objektum elkuldese
            for (SseEmitter emitter : emitters) {
                try {
                    //A kliensnek elkuldom a like es dislike szamot
                    emitter.send(SseEmitter.event().name("LikeOrDislike").data(likeAndDislikeCounts));
                } catch (IOException e) {
                    emitters.remove(emitter);
                }
            }

        } else {
            System.err.println("Invalid message: " + message);
            return ResponseEntity.badRequest().body("Invalid message");
        }

        return ResponseEntity.ok("SSE message sent");
    }

    @PostMapping("/sendLikeOrDislikeForDiploma")
    public ResponseEntity<String> sendSseMessageForDiploma(@RequestBody Map<String, String> data) {
        //System.out.println("SSE message sent(data): " + data);
        String message = data.get("message");
        //System.out.println("SSE message sent1(message): " + message);
        if (message == null || message.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid message");
        }
        //System.out.println("SSE message sent2(message): " + message);

        //A message-t elvalasztjuk a ":" karakterrel
        String[] messageParts = message.split(":");

        if (messageParts.length == 2) {
            //Action kinyerese a message-bol
            String action = messageParts[0];
            //System.out.println("SSE message sent(action): " + action);

            //Diploma ID kinyerese a message-bol
            Long diplomaId = Long.parseLong(messageParts[1]);
            //System.out.println("SSE message sent(diplomaId): " + diplomaId);

            Map<String, Integer> likeAndDislikeCounts = diplomaServices.getLikeAndDislikeCounts(diplomaId);
            //System.out.println("SSE message sent(likeAndDislikeCounts): " + likeAndDislikeCounts);

            // itt az emitters listaban levo osszes SSE objektumot vegig kell iteralni
            // es mindegyik SSE objektumot elkuldeni a kliensnek
            // az emitterben a kovetkezo adatok vannak:
            // - SSE objektum
            // - SSE objektum lezarasa
            // - SSE objektum elkuldese
            for (SseEmitter emitter : emitters) {
                try {
                    //A kliensnek elkuldom a like es dislike szamot
                    emitter.send(SseEmitter.event().name("LikeOrDislike").data(likeAndDislikeCounts));
                } catch (IOException e) {
                    emitters.remove(emitter);
                }
            }

        } else {
            System.err.println("Invalid message: " + message);
            return ResponseEntity.badRequest().body("Invalid message");
        }

        return ResponseEntity.ok("SSE message sent");
    }


    @PostMapping("/sendCommentInMyGroup")
    public ResponseEntity<String> sendSseMessageForCommentInMyGroup(@RequestBody Rating commentData,
                                                                    Principal principal)
    {
        try {
            String ratingUserEmail = principal.getName();
            Long ratingUserId = userRepository.findIdByEmail(ratingUserEmail);
            Long ratedUserId = commentData.getUserId();
            int score = commentData.getScore();
            String comment = commentData.getComment();
            String date = commentData.getDate();

//            System.out.println("ratingUserEmail: " + ratingUserEmail + " ,Id: " + ratingUserId);
//            System.out.println("ratedUserId: " + ratedUserId);
//            System.out.println("score: " + score);
//            System.out.println("comment: " + comment);
//            System.out.println("date: " + date);

            //System.out.println("SSE message sent1(message): " + message);
            if (ratedUserId == null && ratingUserId == null) {
                return ResponseEntity.badRequest().body("Invalid message");
            }

            List<Object[]> userImageAndName = userRepository.findUserImageById(ratingUserId);

            if (userImageAndName != null && !userImageAndName.isEmpty()) {
                Object[] userData = userImageAndName.get(0);

                byte[] userImage = (byte[]) userData[0];
                String firstName = (String) userData[2];
                String lastName = (String) userData[1];

//                System.out.println("userImage: " + userImage);
//                System.out.println("userName: " + userName);

                Map<String, Object> userCommentData = new HashMap<>();
                userCommentData.put("ratingUserId", ratingUserId);
                userCommentData.put("ratedUserId", ratedUserId);
                userCommentData.put("score", score);
                userCommentData.put("comment", comment);
                userCommentData.put("date", date);
                userCommentData.put("userImage", userImage);
                userCommentData.put("firstName", firstName);
                userCommentData.put("lastName", lastName);

                //System.out.println("userCommentData: " + userCommentData);

                for (SseEmitter emitter : emitters) {
                    try {
                        //A kliensnek elkuldom a like es dislike szamot
                        emitter.send(SseEmitter.event().name("UserComment").data(userCommentData));
                    } catch (IOException e) {
                        //e.printStackTrace(); // Logold ki a kivételt
                        emitters.remove(emitter);
                    }
                }

            }
            return ResponseEntity.ok("SSE message sent");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

