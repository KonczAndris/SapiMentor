package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Rating;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.model.Topics_Comment;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.Topics_CommentRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.repository.UserRepository;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.DiplomaServices;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.ExamServices;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.ResourceServices;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.Topics_CommentService;

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
    private final Topics_CommentService topicsCommentService;
    private final Topics_CommentRepository topicsCommentRepository;

    private final UserRepository userRepository;

    public SseController(ResourceServices resourceServices,
                         ExamServices examServices,
                         DiplomaServices diplomaServices,
                         UserRepository userRepository,
                         Topics_CommentService topicsCommentService,
                         Topics_CommentRepository topicsCommentRepository) {
        this.resourceServices = resourceServices;
        this.examServices = examServices;
        this.diplomaServices = diplomaServices;
        this.userRepository = userRepository;
        this.topicsCommentService = topicsCommentService;
        this.topicsCommentRepository = topicsCommentRepository;
    }

    // SSE endpoint
    // itt a felhasznalo feliratkozik a SSE-re
    @GetMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe() {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try {
            sseEmitter.send(SseEmitter.event().name("INIT"));
        } catch (IOException e){
            e.printStackTrace();
        }
        // SSE objektum lezarasa
        sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));

        emitters.add(sseEmitter);
        return sseEmitter;
    }

    @PostMapping("/sendLikeOrDislike")
    public ResponseEntity<String> sendSseMessage(@RequestBody Map<String, String> data) {
        String message = data.get("message");
        if (message == null || message.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid message");
        }

        //A message-t elvalasztjuk a ":" karakterrel
        String[] messageParts = message.split(":");

        if (messageParts.length == 2) {
            //Action kinyerese a message-bol
            String action = messageParts[0];
            Long resourceId = Long.parseLong(messageParts[1]);

            Map<String, Integer> likeAndDislikeCounts = resourceServices.getLikeAndDislikeCounts(resourceId);

            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event().name("LikeOrDislikeLink").data(likeAndDislikeCounts));
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
        String message = data.get("message");

        if (message == null || message.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid message");
        }

        //A message-t elvalasztjuk a ":" karakterrel
        String[] messageParts = message.split(":");

        if (messageParts.length == 2) {
            //Action kinyerese a message-bol
            String action = messageParts[0];
            Long examId = Long.parseLong(messageParts[1]);

            Map<String, Integer> likeAndDislikeCounts = examServices.getLikeAndDislikeCounts(examId);

            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(SseEmitter.event().name("LikeOrDislikeExam").data(likeAndDislikeCounts));
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
        String message = data.get("message");
        if (message == null || message.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid message");
        }

        //A message-t elvalasztjuk a ":" karakterrel
        String[] messageParts = message.split(":");

        if (messageParts.length == 2) {
            //Action kinyerese a message-bol
            String action = messageParts[0];

            Long diplomaId = Long.parseLong(messageParts[1]);

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
                    emitter.send(SseEmitter.event().name("LikeOrDislikeDiploma").data(likeAndDislikeCounts));
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

            if (ratedUserId == null && ratingUserId == null) {
                return ResponseEntity.badRequest().body("Invalid message");
            }

            List<Object[]> userImageAndName = userRepository.findUserImageById(ratingUserId);

            if (userImageAndName != null && !userImageAndName.isEmpty()) {
                Object[] userData = userImageAndName.get(0);

                byte[] userImage = (byte[]) userData[0];
                String firstName = (String) userData[2];
                String lastName = (String) userData[1];

                Map<String, Object> userCommentData = new HashMap<>();
                userCommentData.put("ratingUserId", ratingUserId);
                userCommentData.put("ratedUserId", ratedUserId);
                userCommentData.put("score", score);
                userCommentData.put("comment", comment);
                userCommentData.put("date", date);
                userCommentData.put("userImage", userImage);
                userCommentData.put("firstName", firstName);
                userCommentData.put("lastName", lastName);

                for (SseEmitter emitter : emitters) {
                    try {
                        emitter.send(SseEmitter.event().name("UserComment").data(userCommentData));
                    } catch (IOException e) {
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

    @PostMapping("/sendCommentInTopics")
    public ResponseEntity<String> sendSseMessageForCommentInTopics(@RequestBody Topics_Comment commentData,
                                                                    Principal principal) {
        try {
            String userEmail = principal.getName();
            Long userId = userRepository.findIdByEmail(userEmail);
            String ratedTopicId = commentData.getRatedTopicId();
            String subject = commentData.getSubject();
            String comment = commentData.getComment();
            String date = commentData.getDate();

            if (ratedTopicId == null && userId == null) {
                return ResponseEntity.badRequest().body("Invalid message");
            }

            List<Object[]> userImageAndName = userRepository.findUserImageById(userId);

            if (userImageAndName != null && !userImageAndName.isEmpty()) {
                Object[] userData = userImageAndName.get(0);

                byte[] userImage = (byte[]) userData[0];
                String firstName = (String) userData[2];
                String lastName = (String) userData[1];

                Map<String, Object> userCommentData = new HashMap<>();
                userCommentData.put("ratingUserId", userId);
                userCommentData.put("ratedTopicId", ratedTopicId);
                userCommentData.put("subject", subject);
                userCommentData.put("comment", comment);
                userCommentData.put("date", date);
                userCommentData.put("userImage", userImage);
                userCommentData.put("firstName", firstName);
                userCommentData.put("lastName", lastName);

                for (SseEmitter emitter : emitters) {
                    try {
                        emitter.send(SseEmitter.event().name("UserCommentTopics").data(userCommentData));
                    } catch (IOException e) {
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

