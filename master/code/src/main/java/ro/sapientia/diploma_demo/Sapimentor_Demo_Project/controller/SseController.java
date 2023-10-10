package ro.sapientia.diploma_demo.Sapimentor_Demo_Project.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/sse")
public class SseController {
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        return emitter;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendSseMessage(String message) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().data(message));
            } catch (Exception e) {
                emitter.complete();
            }
        }
        return ResponseEntity.ok("SSE message sent");
    }

    @PostMapping("/send-like")
    public ResponseEntity<String> sendLikeSseMessage(String message) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("like").data(message));
            } catch (Exception e) {
                emitter.complete();
            }
        }
        return ResponseEntity.ok("SSE like message sent");
    }

    @PostMapping("/send-dislike")
    public ResponseEntity<String> sendDislikeSseMessage(String message) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("dislike").data(message));
            } catch (Exception e) {
                emitter.complete();
            }
        }
        return ResponseEntity.ok("SSE dislike message sent");
    }


}

