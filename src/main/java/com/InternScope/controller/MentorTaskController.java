package com.InternScope.controller;

import com.InternScope.model.Task;
import com.InternScope.repository.TaskRepository;
import com.InternScope.service.AIService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mentor/tasks")
public class MentorTaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AIService aiService;

    @PostMapping("/{taskId}/comment")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<?> addCommentAndFeedback(@PathVariable String taskId, @RequestBody CommentRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setMentorComment(request.getMentorComment());
        String aiFeedback = aiService.generateFeedback(task.getDescription(), request.getMentorComment());
        task.setAiFeedback(aiFeedback);

        taskRepository.save(task);
        return ResponseEntity.ok("Comment and AI feedback saved");
    }

    @Data
    static class CommentRequest {
        private String mentorComment;
    }
}