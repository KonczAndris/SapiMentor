package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ro.sapientia.diploma_demo.Sapimentor_Demo_Project.service.ResourceServices;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/sse")
public class SseController {
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ResourceServices resourceServices;

    public SseController(ResourceServices resourceServices) {
        this.resourceServices = resourceServices;
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

    // SSE endpoint
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

}

