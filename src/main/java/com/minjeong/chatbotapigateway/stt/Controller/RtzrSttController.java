package com.minjeong.chatbotapigateway.stt.Controller;

import com.minjeong.chatbotapigateway.stt.service.SttService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stt/v1")
public class RtzrSttController {

    private final SttService sttService;

    @PostMapping(value = "/speech-to-text:recognize")
    public ResponseEntity<?> speechToText(@RequestParam("key") String key, @RequestBody Map<String, Object> data) throws IOException, InterruptedException, JSONException {
        log.info("api key: {}", key);
        if (!key.equals(sttService.getApiKey())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(sttService.recognizeSpeech(data));
    }

    @PostMapping(value = "/encode", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> encodeAudio(@RequestPart MultipartFile audio) throws IOException {
        return ResponseEntity.ok(sttService.encodeWavFileToBase64(audio));
    }
}
