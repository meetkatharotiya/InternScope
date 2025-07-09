package com.InternScope.controller;

import com.InternScope.model.Task;
import com.InternScope.model.User;
import com.InternScope.repository.TaskRepository;
import com.InternScope.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/intern/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/log")
    public ResponseEntity<?> logTask(@AuthenticationPrincipal UserDetails userDetails,
                                     @RequestBody TaskRequest request) {
        User intern = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Task task = Task.builder()
                .intern(intern)
                .date(request.getDate() != null ? request.getDate() : LocalDate.now())
                .description(request.getDescription())
                .screenshots(request.getScreenshots())
                .links(request.getLinks())
                .build();

        taskRepository.save(task);
        return ResponseEntity.ok("Task logged successfully");
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasks(@AuthenticationPrincipal UserDetails userDetails) {
        User intern = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Task> tasks = taskRepository.findByInternOrderByDateDesc(intern);
        return ResponseEntity.ok(tasks);
    }

    @Data
    static class TaskRequest {
        private LocalDate date;
        private String description;
        private List<String> screenshots;
        private List<String> links;
    }

}