package com.placementtracker.ai;

public interface AIProvider {
    /**
     * Generates text using the underlying AI provider.
     * @param prompt The prompt to send to the AI
     * @return The AI-generated text response
     */
    String generateText(String prompt) throws Exception;
}
