package com.InternScope.repository;

import com.InternScope.model.Task;
import com.InternScope.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByInternOrderByDateDesc(User intern);
    List<Task> findByInternAndDateBetweenOrderByDateAsc(User intern, LocalDate start, LocalDate end);
}