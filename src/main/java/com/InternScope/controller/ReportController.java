package com.InternScope.controller;

import com.InternScope.model.Task;
import com.InternScope.model.User;
import com.InternScope.repository.TaskRepository;
import com.InternScope.repository.UserRepository;
import com.InternScope.service.EmailService;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.Resource;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/{internUsername}/pdf")
    public ResponseEntity<byte[]> generatePdfReport(@PathVariable String internUsername) {
        User intern = userRepository.findByUsername(internUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Task> tasks = taskRepository.findByInternOrderByDateDesc(intern);

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            // Title
            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 20, com.itextpdf.text.Font.BOLD);
            document.add(new Paragraph("Internship Report", titleFont));
            document.add(new Paragraph(" ")); // Empty line

            // Subtitle
            com.itextpdf.text.Font subFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD);
            document.add(new Paragraph("Intern: " + intern.getUsername(), subFont));
            document.add(new Paragraph("Generated on: " + LocalDate.now()));
            document.add(new Paragraph(" ")); // Empty line

            com.itextpdf.text.Font labelFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font valueFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12);

            for (Task task : tasks) {
                document.add(new Paragraph("Task Date: ", labelFont));
                document.add(new Paragraph(String.valueOf(task.getDate()), valueFont));
                document.add(new Paragraph("Description: ", labelFont));
                document.add(new Paragraph(task.getDescription(), valueFont));
                if (task.getMentorComment() != null) {
                    document.add(new Paragraph("Mentor Comment: ", labelFont));
                    document.add(new Paragraph(task.getMentorComment(), valueFont));
                }
                if (task.getAiFeedback() != null) {
                    document.add(new Paragraph("AI Feedback: ", labelFont));
                    document.add(new Paragraph(task.getAiFeedback(), valueFont));
                }
                // Add a separator line
                document.add(new Paragraph("------------------------------------------------------------"));
                document.add(new Paragraph(" ")); // Extra space
            }

            document.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("Internship_Report_" + intern.getUsername() + ".pdf")
                    .build());

            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
