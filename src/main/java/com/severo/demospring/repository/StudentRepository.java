package com.severo.demospring.repository;


import com.severo.demospring.domain.Student;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Repository
public class StudentRepository {
    private static List<Student> students;
    static {
        students = new ArrayList<>(List.of(
                new Student(1, "Joao"),
                new Student(2, "Maria"),
                new Student(3, "Pedro")));
    }

    public List<Student> listAll() {
        return students;
    }

    public Student save(Student student) {
        student.setId(ThreadLocalRandom.current().nextInt(4, 100000));
        students.add(student);
        return student;
    }
}
