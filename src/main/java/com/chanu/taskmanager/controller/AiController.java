package com.chanu.taskmanager.controller;

import org.springframework.web.bind.annotation.*;
import com.chanu.taskmanager.service.AiService;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:5173")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/description")
    public String generateDescription(@RequestParam String title) {
        return aiService.generateDescription(title);
    }

    @PostMapping("/priority")
    public String suggestPriority(
            @RequestParam String title,
            @RequestParam(required = false, defaultValue = "") String description) {

        return aiService.suggestPriority(title, description);
    }

    @PostMapping("/estimate")
    public String estimateCompletionTime(
            @RequestParam String title,
            @RequestParam(required = false, defaultValue = "") String description) {

        return aiService.estimateCompletionTime(title, description);
    }
}