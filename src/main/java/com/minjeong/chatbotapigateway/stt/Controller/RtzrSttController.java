package com.minjeong.chatbotapigateway.stt.Controller;

import com.minjeong.chatbotapigateway.stt.service.SttService;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stt/v1/rtzr")
public class RtzrSttController {

    private final SttService sttService;

    @PostMapping(value = "/recognize", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> speechToText(@RequestParam("key") String apiKey, @RequestPart List<MultipartFile> audio) throws IOException, InterruptedException, JSONException {
        if (!apiKey.equals(sttService.getApiKey())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(sttService.recognizeSpeech(audio));
    }
}
