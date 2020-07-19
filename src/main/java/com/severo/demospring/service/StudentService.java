package com.severo.demospring.service;


import com.severo.demospring.domain.Student;
import com.severo.demospring.repository.StudentRepository;
import com.severo.demospring.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudentService {

    private final Utils utils;
    private final StudentRepository studentRepository;

    public List<Student> listAll() {
        return studentRepository.findAll();
    }

    public List<Student> findByName(String name) {
        return studentRepository.findByName(name);
    }

    public Student findById(int id) {
        return utils.findStudentOrThrowNotFound(id, studentRepository);
    }

    @Transactional
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public void delete(int id) {
        studentRepository.delete(utils.findStudentOrThrowNotFound(id, studentRepository));
    }

    public void update(Student student) {
        studentRepository.save(student);
    }
}
