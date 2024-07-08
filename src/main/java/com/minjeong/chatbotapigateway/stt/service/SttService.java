package com.minjeong.chatbotapigateway.stt.service;

import com.minjeong.chatbotapigateway.stt.WavHeader;
import com.minjeong.chatbotapigateway.stt.dto.SttResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class SttService {

    private boolean stopPolling = false;
    private String transcribeId = null;
    private String accessToken = null;

    private final String RTZR_STT_API_URL = "https://openapi.vito.ai/v1";
    private final String RTZR_CLIENT_ID = "YmP9QuwUQsmIJj3bakTl";
    private final String RTZR_CLIENT_SECRET = "qBN5K1-_9g1Mu5j8lImz_m-7EBNTQ1V7ffKIx_W1";
    private final String BP_API_KEY = "9845y2guwhvleajtp2";

    public SttService() {
        WebClient webClient = WebClient.builder()
                .baseUrl(RTZR_STT_API_URL)
                .build();
    }

    // header 의 API key 확인
    public String getApiKey() {
        return BP_API_KEY;
    }

    // PCM -> WAV file 변환 (return 값 : File)
    public File convertPcmToWav(byte[] audioBytes) {
        File wavFile = new File("audio.wav");

        try {
            byte[] wavData = new byte[44 + audioBytes.length];

            // WAV header
            ByteBuffer buffer = ByteBuffer.wrap(wavData);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            buffer.put("RIFF".getBytes());
            buffer.putInt(36 + audioBytes.length);
            buffer.put("WAVEfmt ".getBytes());
            buffer.putInt(16);
            buffer.putShort((short) 1);
            buffer.putShort((short) 1);
            buffer.putInt(16000);
            buffer.putInt(32000);
            buffer.putShort((short) 2);
            buffer.putShort((short) 16);
            buffer.put("data".getBytes());
            buffer.putInt(audioBytes.length);

            // PCM data
            System.arraycopy(audioBytes, 0, wavData, 44, audioBytes.length);

            Files.write(wavFile.toPath(), wavData);
        } catch (IOException e) {
            log.error("convertPcmToWav Error: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return wavFile;
    }

//    public File convert(byte[] audio) {
//        byte[] wavData = WavHeader.wavHeader(audio, 1);
//
//        // 현재 날짜 & 시간을 이용해 unique한 wave 파일명 생성
//        String fileName = System.currentTimeMillis() + ".wav";
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream("./wav/" + fileName);
//            fos.write(wavData);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return new File("./wav/" + fileName);
//    }

    public Object recognizeSpeech(Map<String, Object> data) throws JSONException, IOException, InterruptedException {
        log.info("## recognizeSpeech method");
        // Map 으로 받은 data를 JSONObject 로 변환
        JSONObject request = new JSONObject(data);

        JSONObject jsonObject = request.getJSONObject("audio");
        log.info("jsonObject.get(content) : "+ jsonObject.get("content"));
        byte[] audioBytes = Base64.getDecoder().decode(jsonObject.getString("content"));

        return transcribeFile(audioBytes);
    }

    // rtzr token 발급
    public String getAccessToken() throws JSONException {
        WebClient webClient = WebClient.builder()
                .baseUrl(RTZR_STT_API_URL)
                .build();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", RTZR_CLIENT_ID);
        formData.add("client_secret", RTZR_CLIENT_SECRET);

        String response = webClient.post()
                .uri("/authenticate")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("getAccessToken - Access token : " + response);
        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getString("access_token");
    }

    // rtzr stt 호출 -> return id
    public Object transcribeFile(byte[] audioBytes) throws IOException, InterruptedException, JSONException {

        log.info("TranscribeFile start time : " + System.currentTimeMillis());
        accessToken = getAccessToken();
        log.info("Access token : " + accessToken);

        WebClient webClient = WebClient.builder()
                .baseUrl(RTZR_STT_API_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.MULTIPART_FORM_DATA))
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .build();

        File file = convertPcmToWav(audioBytes);
        log.info("File : {}, {}", file.getName(), file.getAbsolutePath());

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", new FileSystemResource(file));
        builder.part("config", "{}");

        String response = null;

        log.info("transcribe post method start time : " + System.currentTimeMillis());
        response = webClient.post()
                .uri("/transcribe")
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        log.info("Finish transcribe post method time : " + System.currentTimeMillis());

        // local 에 저장되는 file 삭제
        try {
            Files.delete(file.toPath());
            log.info("File deleted");
        } catch (IOException e) {
            log.info("Error in deleting file");
            log.info(e.toString());
        }

        log.info("Response : " + response);
        JSONObject jsonObject = new JSONObject(response);
        log.info("jsonObject : " + jsonObject);

        // token 6시간 지나면 만료 -> access token 다시 발급
        try {
            if (jsonObject.getString("id") == null && jsonObject.getString("code").equals("H0002")) {
                log.info("Access token expired. Get new access token");
                accessToken = getAccessToken();
                response = webClient.post()
                        .uri("/transcribe")
                        .body(BodyInserters.fromMultipartData(builder.build()))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
            }
        } catch (JSONException e) {
            log.info("Error in checking code");
            log.info(e.toString());
        }

        log.info("Transcribe request id : " + jsonObject.getString("id"));

        stopPolling = false;
        transcribeId = jsonObject.getString("id");
        return getTranscribeResult();
    }

    // rtzr stt 결과 호출
    public Object getTranscribeResult() throws InterruptedException, JSONException {
        log.info("polling start time : " + System.currentTimeMillis());
        String response = null;
        Thread.sleep(1000);

        while (!stopPolling) {
            log.info("polling...");

            WebClient webClient = WebClient.builder()
                    .baseUrl(RTZR_STT_API_URL)
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .build();

            String uri = "/transcribe/" + transcribeId;

            log.info("transcribe ID get method time : " + System.currentTimeMillis());
            response = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("response : " + response);
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getString("status").equals("completed")) {
                stopPolling = true;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            log.info("while polling... end");
        }

        // json parsing
        log.info("polling end time : " + System.currentTimeMillis());
        log.info("response : " + response);

        return getResultJson(response);
    }

    private String getResultJson(String response) {
        JSONObject jsonObject = new JSONObject(response);
        JSONObject resultsObject = jsonObject.getJSONObject("results");
        JSONArray utterances = resultsObject.getJSONArray("utterances");
        String transcript = utterances.getJSONObject(0).getString("msg");

        JSONObject resultJson = new JSONObject();
        JSONArray resultsArray = new JSONArray();
        JSONObject alternatives = new JSONObject();
        JSONArray alternativesArray = new JSONArray();

        alternatives.put("transcript", transcript);
        alternatives.put("confidence", 1.0);
        alternatives.put("words", new JSONArray());
        alternativesArray.put(alternatives);

        JSONObject resultItem = new JSONObject();
        resultItem.put("alternatives", alternativesArray);
        resultItem.put("channelTag", 1);
        resultItem.put("resultEndTime", "3.5s");
        resultItem.put("languageCode", "ko-KR");

        resultsArray.put(resultItem);

        resultJson.put("results", resultsArray);
        resultJson.put("totalBilledTime", "3.5s");
        return resultJson.toString();
    }

//     wav file base64 encoding
    public byte[] encodeWavFileToBase64(MultipartFile file) throws IOException {
        return Base64.getEncoder().encode(file.getBytes());
    }

}
