package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/sse")
public class SseController {
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    // SSE endpoint
    // itt a felhasznalo feliratkozik a SSE-re
    // a feliratkozas utan a kliens kap egy SSE objektumot
    // amit a kliens eltarol
    // a kliensnek a SSE objektumot kell hasznalnia a kliens es a szerver kozotti kommunikaciohoz
    @GetMapping(value = "/subscribe", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe() {
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        try {
            sseEmitter.send(SseEmitter.event().name("INIT"));
        } catch (IOException e){
            e.printStackTrace();
        }
        sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));

        emitters.add(sseEmitter);
        return sseEmitter;
    }


    // SSE endpoint
    // itt a szerver elkuldi a kliensnek az uzenetet
    // a kliensnek a SSE objektumot kell hasznalnia a kliens es a szerver kozotti kommunikaciohoz
    @PostMapping("/sendLikeOrDislike")
    public ResponseEntity<String> sendSseMessage(String message) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("Igen").data(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok("SSE message sent");
    }
}

