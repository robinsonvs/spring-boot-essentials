package com.severo.demospring.controller;

import com.severo.demospring.domain.Student;
import com.severo.demospring.repository.StudentRepository;
import com.severo.demospring.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("students")
@Slf4j
@RequiredArgsConstructor
public class StudentController {

    private final DateUtil dateUtil;
    private final StudentRepository studentRepository;

    @GetMapping
    public ResponseEntity<List<Student>> listAll() {
        log.info("Date formatted {}", dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(studentRepository.listAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Student> findById(@PathVariable int id) {

        Student studentFound = studentRepository.listAll()
                .stream()
                .filter(student -> student.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));

        return ResponseEntity.ok(studentFound);
    }

    @PostMapping
    public ResponseEntity<Student> save(@RequestBody Student student) {
        return ResponseEntity.ok(studentRepository.save(student));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        studentRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
