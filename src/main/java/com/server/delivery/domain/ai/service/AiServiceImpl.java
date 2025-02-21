package com.server.delivery.domain.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.delivery.model.ai.entity.AiResponse;
import com.server.delivery.model.ai.repository.AiResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final RestTemplate restTemplate;
    private final AiResponseRepository aiResponseRepository;

    @Value("${google.ai.api.key}")
    private String apiKey;


    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";

    public String generateContent(String content) {
        Map<String, Object> request = new HashMap<>();
        List<Map<String, Object>> contents = new ArrayList<>();
        Map<String, Object> parts = new HashMap<>();
        parts.put("text", content + "답변을 최대한 간결하게 50자 이하로");
        contents.add(Collections.singletonMap("parts", Collections.singletonList(parts)));
        request.put("contents", contents);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(request, headers);

        String fullurl = API_URL + "?key=" + apiKey;

        ResponseEntity<String> response = restTemplate.exchange(fullurl, HttpMethod.POST, requestEntity, String.class);

        String responseText = extractResponseText(response.getBody());

        AiResponse aiResponse = AiResponse.builder()
                .responseText(responseText)
                .build();

        aiResponseRepository.save(aiResponse);

        return responseText;

    }

    private String extractResponseText(String jsonResponse) {
        try{
            Map<String, Object> responseMap = new ObjectMapper().readValue(jsonResponse, Map.class);
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseMap.get("candidates");

            if(candidates != null && !candidates.isEmpty()) {
                Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");

                if(parts != null && !parts.isEmpty()) {
                    return (String) parts.get(0).get("text");
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "응답을 처리할 수 없습니다.";
    }


}
