package com.reafreitas1.iastudy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class StudyService {

    private static final Logger logger = LoggerFactory.getLogger(StudyService.class);

    private final ChatClient chatClient;

    public StudyService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public StudyResponse processText(String text, int quantity) {

        logger.info("Receiving request: '{}' with {} flashcards.", text, quantity);

        String promptInstruction = """
            You are a highly didactic and organized teaching assistant.
            
            INSTRUCTIONS:
            1. Create a detailed summary divided into several independent topics.
            2. Each topic must be a separate string in the "summary" array.
            3. Generate exactly %d flashcards.
            
            LANGUAGE RULE:
            - Respond in the same language as the USER INPUT.
            - IMPORTANT: Even if the content is in another language, the JSON KEYS must remain in English: "summary", "flashcards", "question", "answer".
            
            OUTPUT RULES:
            - Return ONLY the JSON object.
            - DO NOT include markdown blocks like ```json or any extra text.
            
            FORMAT EXAMPLE:
            {
                "summary": [
                    "Topic one about the subject.",
                    "Topic two with more details."
                    "Third concept to remember."
                ],
                "flashcards": [
                    { "question": "...", "answer": "..." }
              ]
            }
            
            USER INPUT:
            %s
            """.formatted(quantity, text);

        try {
        
        logger.info("Sending request to the AI...");

        StudyResponse response = chatClient.prompt(promptInstruction)
                .call()
                .entity(StudyResponse.class);

        
        logger.info("Processing completed successfully!");
        
        return response;

    } catch (RuntimeException e) {
            logger.error("Error processing the AI's response: {}", e.getMessage());
            e.printStackTrace();
            throw new StudyProcessingException("Error processing AI response", e);
        }
    }

    public static class StudyProcessingException extends RuntimeException {
        public StudyProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}