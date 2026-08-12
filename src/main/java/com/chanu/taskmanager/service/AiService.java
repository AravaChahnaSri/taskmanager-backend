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

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateDescription(String title) {

        String url =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        String prompt = """
            You are an AI task management assistant.

            Create a concise and practical description for the following task.

            Rules:
            - Return ONLY the task description.
            - Do NOT ask questions.
            - Do NOT provide options or templates.
            - Do NOT use markdown.
            - Do NOT mention that you are an AI.
            - Keep it to 1-2 clear sentences.
            - Clearly describe what the user needs to accomplish.

            Task title: %s
            """.formatted(title);

        Map<String, Object> textPart = Map.of(
            "text", prompt
        );

        Map<String, Object> contents = Map.of(
            "parts", new Object[]{textPart}
        );

        Map<String, Object> requestBody = Map.of(
            "contents", new Object[]{contents}
        );

        HttpEntity<Map<String, Object>> request =
            new HttpEntity<>(requestBody, headers);

        Map<String, Object> response =
            restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Map.class
            ).getBody();

        try {
            Map<String, Object> candidate =
                (Map<String, Object>) ((java.util.List<?>) response.get("candidates")).get(0);

            Map<String, Object> content =
                (Map<String, Object>) candidate.get("content");

            java.util.List<?> parts =
                (java.util.List<?>) content.get("parts");

            Map<String, Object> part =
                (Map<String, Object>) parts.get(0);

            return part.get("text").toString().trim();

        } catch (Exception e) {
            return "Unable to generate description.";
        }
    }
    public String suggestPriority(String title, String description) {

        String url =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        String prompt = """
            Analyze the following task and suggest its priority.

            Task title: %s
            Task description: %s

            Return ONLY one of these values:
            LOW
            MEDIUM
            HIGH

            Do not provide any explanation.
            """.formatted(title, description);

        Map<String, Object> textPart = Map.of(
            "text", prompt
        );

        Map<String, Object> contents = Map.of(
            "parts", new Object[]{textPart}
        );

        Map<String, Object> requestBody = Map.of(
            "contents", new Object[]{contents}
        );

        HttpEntity<Map<String, Object>> request =
            new HttpEntity<>(requestBody, headers);

        Map<String, Object> response =
            restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Map.class
            ).getBody();

        try {
            Map<String, Object> candidate =
                (Map<String, Object>) ((java.util.List<?>) response.get("candidates")).get(0);

            Map<String, Object> content =
                (Map<String, Object>) candidate.get("content");

            java.util.List<?> parts =
                (java.util.List<?>) content.get("parts");

            Map<String, Object> part =
                (Map<String, Object>) parts.get(0);

            return part.get("text").toString().trim().toUpperCase();

        } catch (Exception e) {
            return "MEDIUM";
        }
    }
    public String estimateCompletionTime(String title, String description) {

        String url =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", apiKey);

        String prompt = """
            Estimate how long it would reasonably take to complete the following task.

            Task title: %s
            Task description: %s

            Return ONLY a short time estimate such as:
            30 minutes
            2 hours
            1 day
            2-3 days

            Do not provide any explanation.
            """.formatted(title, description);

        Map<String, Object> textPart = Map.of(
            "text", prompt
        );

        Map<String, Object> contents = Map.of(
            "parts", new Object[]{textPart}
        );

        Map<String, Object> requestBody = Map.of(
            "contents", new Object[]{contents}
        );

        HttpEntity<Map<String, Object>> request =
            new HttpEntity<>(requestBody, headers);

        Map<String, Object> response =
            restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                Map.class
            ).getBody();

        try {
            Map<String, Object> candidate =
                (Map<String, Object>) ((java.util.List<?>) response.get("candidates")).get(0);

            Map<String, Object> content =
                (Map<String, Object>) candidate.get("content");

            java.util.List<?> parts =
                (java.util.List<?>) content.get("parts");

            Map<String, Object> part =
                (Map<String, Object>) parts.get(0);

            return part.get("text").toString().trim();

        } catch (Exception e) {
            return "Unable to estimate completion time.";
        }
    }
}