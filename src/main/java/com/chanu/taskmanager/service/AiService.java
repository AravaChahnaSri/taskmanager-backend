package com.chanu.taskmanager.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AiService {

    @Value("${gemini.api.key:}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateDescription(String title) {
        try {
            if (apiKey != null && !apiKey.isBlank()) {
                String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("x-goog-api-key", apiKey);

                String prompt = """
                    You are an AI task management assistant.
                    Create a concise and practical description for the following task.
                    Rules:
                    - Return ONLY the task description.
                    - Keep it to 1-2 clear sentences.
                    Task title: %s
                    """.formatted(title);

                Map<String, Object> textPart = Map.of("text", prompt);
                Map<String, Object> contents = Map.of("parts", new Object[]{textPart});
                Map<String, Object> requestBody = Map.of("contents", new Object[]{contents});

                HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

                Map<String, Object> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class).getBody();

                if (response != null && response.containsKey("candidates")) {
                    Map<String, Object> candidate = (Map<String, Object>) ((java.util.List<?>) response.get("candidates")).get(0);
                    Map<String, Object> content = (Map<String, Object>) candidate.get("content");
                    java.util.List<?> parts = (java.util.List<?>) content.get("parts");
                    Map<String, Object> part = (Map<String, Object>) parts.get(0);
                    return part.get("text").toString().trim();
                }
            }
        } catch (Exception e) {
            System.err.println("Gemini API Error: " + e.getMessage());
        }

        // Smart Fallback if API fails or key missing
        return "Complete all required deliverables and action items for " + title + ". Ensure thorough review and quality execution.";
    }

    public String suggestPriority(String title, String description) {
        try {
            if (apiKey != null && !apiKey.isBlank()) {
                String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("x-goog-api-key", apiKey);

                String prompt = """
                    Analyze the following task and suggest its priority.
                    Task title: %s
                    Task description: %s
                    Return ONLY one of these values: LOW, MEDIUM, HIGH
                    """.formatted(title, description);

                Map<String, Object> textPart = Map.of("text", prompt);
                Map<String, Object> contents = Map.of("parts", new Object[]{textPart});
                Map<String, Object> requestBody = Map.of("contents", new Object[]{contents});

                HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

                Map<String, Object> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class).getBody();

                if (response != null && response.containsKey("candidates")) {
                    Map<String, Object> candidate = (Map<String, Object>) ((java.util.List<?>) response.get("candidates")).get(0);
                    Map<String, Object> content = (Map<String, Object>) candidate.get("content");
                    java.util.List<?> parts = (java.util.List<?>) content.get("parts");
                    Map<String, Object> part = (Map<String, Object>) parts.get(0);
                    String result = part.get("text").toString().trim().toUpperCase();
                    if (result.contains("HIGH")) return "HIGH";
                    if (result.contains("LOW")) return "LOW";
                    return "MEDIUM";
                }
            }
        } catch (Exception e) {
            System.err.println("Gemini Priority Error: " + e.getMessage());
        }

        // Smart Fallback
        String t = (title + " " + description).toLowerCase();
        if (t.contains("urgent") || t.contains("bug") || t.contains("critical") || t.contains("client") || t.contains("deploy")) {
            return "HIGH";
        }
        if (t.contains("minor") || t.contains("test") || t.contains("clean")) {
            return "LOW";
        }
        return "MEDIUM";
    }

    public String estimateCompletionTime(String title, String description) {
        try {
            if (apiKey != null && !apiKey.isBlank()) {
                String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("x-goog-api-key", apiKey);

                String prompt = """
                    Estimate how long it would reasonably take to complete the following task.
                    Task title: %s
                    Task description: %s
                    Return ONLY a short time estimate like: 2 hours, 1 day, etc.
                    """.formatted(title, description);

                Map<String, Object> textPart = Map.of("text", prompt);
                Map<String, Object> contents = Map.of("parts", new Object[]{textPart});
                Map<String, Object> requestBody = Map.of("contents", new Object[]{contents});

                HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

                Map<String, Object> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class).getBody();

                if (response != null && response.containsKey("candidates")) {
                    Map<String, Object> candidate = (Map<String, Object>) ((java.util.List<?>) response.get("candidates")).get(0);
                    Map<String, Object> content = (Map<String, Object>) candidate.get("content");
                    java.util.List<?> parts = (java.util.List<?>) content.get("parts");
                    Map<String, Object> part = (Map<String, Object>) parts.get(0);
                    return part.get("text").toString().trim();
                }
            }
        } catch (Exception e) {
            System.err.println("Gemini Estimate Error: " + e.getMessage());
        }

        // Smart Fallback
        return "2-4 hours";
    }
}