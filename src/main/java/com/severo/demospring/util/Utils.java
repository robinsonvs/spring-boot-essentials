package com.severo.demospring.util;

import com.severo.demospring.domain.Student;
import com.severo.demospring.exception.ResourceNotFoundException;
import com.severo.demospring.repository.StudentRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class Utils {

    public String formatLocalDateTimeToDatabaseStyle(LocalDateTime localDateTime) {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDateTime);
    }

    public Student findStudentOrThrowNotFound(int id, StudentRepository studentRepository) {
        return studentRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
    }
}
