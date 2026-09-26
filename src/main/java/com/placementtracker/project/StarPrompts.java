package com.placementtracker.project;

public class StarPrompts {

    public static final String IMPROVE_FIELD_PROMPT = 
        "You are an expert technical recruiter and interview coach. " +
        "I am writing a STAR (Situation, Task, Action, Result) response for an interview. " +
        "Here is my draft for the '%s' section:\n\n%s\n\n" +
        "Please rewrite this to be clearer, more impactful, and more quantifiable where possible. " +
        "Keep it concise (1-3 sentences) and professional. " +
        "Return ONLY the rewritten text, without any explanation, markdown, or quotation marks.";

    public static final String SCORE_STAR_PROMPT = 
        "You are an expert technical recruiter and interview coach. " +
        "Please evaluate the following STAR (Situation, Task, Action, Result) interview response:\n\n" +
        "Situation: %s\n" +
        "Task: %s\n" +
        "Action: %s\n" +
        "Result: %s\n\n" +
        "Provide your evaluation in the following strict JSON format, and return ONLY the JSON string. " +
        "Do not include markdown code blocks or any other text.\n" +
        "{\n" +
        "  \"score\": \"<A grade like A, B, C, or Needs Improvement>\",\n" +
        "  \"feedback\": [\n" +
        "    \"<bullet point 1>\",\n" +
        "    \"<bullet point 2>\",\n" +
        "    \"<bullet point 3>\"\n" +
        "  ]\n" +
        "}";
}
