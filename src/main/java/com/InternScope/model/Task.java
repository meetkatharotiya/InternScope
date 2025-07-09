package com.InternScope.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    @Id
    private String id;

    @DBRef
    private User intern;

    private LocalDate date;

    private String description;

    private List<String> screenshots;

    private List<String> links;

    private String mentorComment;

    private String aiFeedback;
}