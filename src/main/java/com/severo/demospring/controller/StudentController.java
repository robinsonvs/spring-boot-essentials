package com.severo.demospring.controller;

import com.severo.demospring.domain.Student;
import com.severo.demospring.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Operation(summary = "List all paginated and sorted",
    description = "To use pagination and sort add the params ?page='number'&sort='field' to the url",
    tags = {"student"})
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
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
