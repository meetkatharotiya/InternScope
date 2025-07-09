package com.InternScope.controller;

import com.InternScope.model.Task;
import com.InternScope.repository.TaskRepository;
import com.InternScope.repository.UserRepository;
import com.InternScope.service.AIService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import com.InternScope.service.EmailService;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/mentor")
public class MentorTaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AIService aiService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private EmailService emailService;

    @PostMapping("tasks/assign")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<?> assignTaskToIntern(@RequestBody AssignTaskRequest request) {
        // Find intern by username
        var internOpt = userRepository.findByUsername(request.getInternUsername());
        if (internOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Intern not found");
        }
        var intern = internOpt.get();

        Task task = Task.builder()
                .intern(intern)
                .date(request.getDate())
                .description(request.getDescription())
                .screenshots(request.getScreenshots())
                .links(request.getLinks())
                .build();
        taskRepository.save(task);
        return ResponseEntity.ok("Task assigned to intern");
    }

    @PostMapping("tasks/{taskId}/comment")
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

    @GetMapping("tasks/all")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<?> getAllTasks(@RequestParam(value = "internUsername", required = false) String internUsername) {
        if (internUsername != null && !internUsername.isBlank()) {
            var internOpt = userRepository.findByUsername(internUsername);
            if (internOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Intern not found");
            }
            var intern = internOpt.get();
            return ResponseEntity.ok(taskRepository.findByInternOrderByDateDesc(intern));
        } else {
            return ResponseEntity.ok(taskRepository.findAll());
        }
    }

    @PostMapping("/schedule-meeting")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<?> scheduleMeeting(@RequestBody MeetingRequest request) {
        // Validate request
        if (request.getInternEmail() == null || request.getInternEmail().isBlank() ||
                request.getMeetingDateTime() == null || request.getJoinLink() == null || request.getJoinLink().isBlank()) {
            return ResponseEntity.badRequest().body("Invalid request: All fields are required");
        }

        // Parse and validate date-time
        LocalDateTime meetingTime;
        try {
            meetingTime = LocalDateTime.parse(request.getMeetingDateTime());
            if (meetingTime.isBefore(LocalDateTime.now())) {
                return ResponseEntity.badRequest().body("Meeting time cannot be in the past");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date-time format. Use ISO format (e.g., 2025-06-10T10:00:00)");
        }

        // Format meeting time for email (e.g., "June 10, 2025 at 10:00 AM")
        String formattedMeetingTime = meetingTime.format(DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a"));

        // Load HTML email template
        String emailBody;
        try {
            Resource resource = resourceLoader.getResource("classpath:templates/meeting-email.html");
            String template = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            emailBody = String.format(template, formattedMeetingTime, request.getJoinLink(), request.getJoinLink());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to load email template");
        }

        // Send email
        boolean emailSent = emailService.sendEmail(
                request.getInternEmail(),
                "Performance Review Meeting Scheduled",
                emailBody);

        if (!emailSent) {
            return ResponseEntity.status(500).body("Failed to send email");
        }

        return ResponseEntity.ok("Meeting scheduled and email sent successfully");
    }

    @Data
    static class AssignTaskRequest {
        private String internUsername;
        private LocalDate date;
        private String description;
        private List<String> screenshots;
        private List<String> links;
    }

    @Data
    static class CommentRequest {
        private String mentorComment;
    }

    @Data
    static class MeetingRequest {
        private String internEmail;
        private String meetingDateTime; // Expected in ISO format, e.g., "2025-06-10T10:00:00"
        private String joinLink;
    }
}