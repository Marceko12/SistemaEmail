package com.system.emails.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String API_URL =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";


    public String generarRespuesta(String mensaje) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> content = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(
                                    Map.of("text", mensaje)
                            ))
                    )
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(content, headers);

            String url = API_URL + apiKey;

            System.out.println("URL final = " + url);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, Map.class
            );

            System.out.println("RAW RESPONSE = " + response.getBody());

            List candidates = (List) response.getBody().get("candidates");
            Map first = (Map) candidates.get(0);
            Map contentMap = (Map) first.get("content");
            List parts = (List) contentMap.get("parts");
            Map part = (Map) parts.get(0);

            return part.get("text").toString();

        } catch (Exception e) {
            return "Error al procesar mensaje: " + e.getMessage();
        }
    }
}
