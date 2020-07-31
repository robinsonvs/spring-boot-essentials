package com.severo.demospring.controller;

import com.severo.demospring.domain.Student;
import com.severo.demospring.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("students")
@Slf4j
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;


    @GetMapping
    public ResponseEntity<Page<Student>> listAll(Pageable pageable) {
        return ResponseEntity.ok(studentService.listAll(pageable));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Student> findById(@PathVariable int id) {
        return ResponseEntity.ok(studentService.findById(id));
    }

//    @GetMapping(path = "/{id}")
//    public ResponseEntity<Student> findById(@PathVariable int id, @AuthenticationPrincipal UserDetails userDetails) {
//        log.info("user logged in {}", userDetails);
//        return ResponseEntity.ok(studentService.findById(id));
//    }

    @GetMapping(path = "/find")
    public ResponseEntity<List<Student>> findByName(@RequestParam(value = "name") String name) {
        return ResponseEntity.ok(studentService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<Student> save(@Valid @RequestBody Student student) {
        return ResponseEntity.ok(studentService.save(student));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        studentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody Student student) {
        studentService.update(student);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
