package com.InternScope.service;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class AIService {
    @Autowired
    private ChatClient chatClient;

    public String generateFeedback(String internDescription, String mentorComment) {
        String comment = (mentorComment == null || mentorComment.trim().isEmpty())
                ? "No feedback provided" : mentorComment.trim();

        // Construct prompt
        String prompt = String.format(
                "Analyze this mentor comment about an intern's work: \"%s\".\n" +
                        "Generate brief feedback (under 50 words) in markdown with three sections:\n" +
                        "- Strengths: One positive point or effort noted.\n" +
                        "- Improvements: One area to refine based on the comment.\n" +
                        "- Next Steps: One actionable recommendation.",
                comment
        );
        // Simulate AI response
        String aiResponse = chatClient.call(new Prompt(prompt))
                .getResult()
                .getOutput()
                .getContent();

        return aiResponse != null && !aiResponse.isBlank()
                ? aiResponse
                : "AI was not able to answer your question.";

    }
}