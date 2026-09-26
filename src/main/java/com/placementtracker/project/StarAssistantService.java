package com.placementtracker.project;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.placementtracker.ai.AIProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StarAssistantService {

    private final AIProvider aiProvider;
    private final ObjectMapper objectMapper;

    public StarAssistantService(AIProvider aiProvider) {
        this.aiProvider = aiProvider;
        this.objectMapper = new ObjectMapper();
    }

    public ImproveResponse improveField(String fieldName, String currentText) throws Exception {
        if (currentText == null || currentText.trim().isEmpty()) {
            throw new IllegalArgumentException("Input text cannot be empty.");
        }
        if (currentText.length() > 2000) {
            throw new IllegalArgumentException("Input text is too long.");
        }
        
        String prompt = String.format(StarPrompts.IMPROVE_FIELD_PROMPT, fieldName, currentText);
        String suggestion = aiProvider.generateText(prompt);
        return new ImproveResponse(suggestion.trim());
    }

    public ScoreResponse scoreStar(StarForm starForm) throws Exception {
        if (!starForm.isComplete()) {
            throw new IllegalArgumentException("STAR form must be complete before scoring.");
        }
        
        String prompt = String.format(StarPrompts.SCORE_STAR_PROMPT, 
            starForm.getSituation(), 
            starForm.getTask(), 
            starForm.getAction(), 
            starForm.getResult());
            
        String jsonResult = aiProvider.generateText(prompt).trim();
        
        // Strip markdown code blocks if the AI accidentally added them
        if (jsonResult.startsWith("```json")) {
            jsonResult = jsonResult.substring(7);
        } else if (jsonResult.startsWith("```")) {
            jsonResult = jsonResult.substring(3);
        }
        if (jsonResult.endsWith("```")) {
            jsonResult = jsonResult.substring(0, jsonResult.length() - 3);
        }
        jsonResult = jsonResult.trim();
        
        JsonNode root = objectMapper.readTree(jsonResult);
        String score = root.path("score").asText();
        List<String> feedback = new ArrayList<>();
        root.path("feedback").forEach(node -> feedback.add(node.asText()));
        
        return new ScoreResponse(score, feedback);
    }
    
    public record ImproveResponse(String suggestion) {}
    public record ScoreResponse(String score, List<String> feedback) {}
}
