package com.severo.demospring.repository;


import com.severo.demospring.domain.Student;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentRepository {

    public List<Student> listAll() {
        return List.of(new Student(1, "Joao"), new Student(2, "Maria"), new Student(3, "Pedro"));
    }
}
