package com.severo.demospring.repository;

import com.severo.demospring.domain.Student;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Student repository test")
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    @DisplayName("Save creates student when successful")
    public void savePersistStudentWhenSuccessful() {
        Student student = createStudent();

        Student savedStudent = this.studentRepository.save(student);
        Assertions.assertThat(savedStudent.getId()).isNotNull();
        Assertions.assertThat(savedStudent.getName()).isNotNull();
        Assertions.assertThat(savedStudent.getName()).isEqualTo(student.getName());
    }


    @Test
    @DisplayName("Save updates student when successful")
    public void saveUpdateStudentWhenSuccessful() {
        Student student = createStudent();

        Student savedStudent = this.studentRepository.save(student);

        savedStudent.setName("The name updated");

        Student updatedStudent = this.studentRepository.save(savedStudent);

        Assertions.assertThat(savedStudent.getId()).isNotNull();
        Assertions.assertThat(savedStudent.getName()).isNotNull();
        Assertions.assertThat(savedStudent.getName()).isEqualTo(updatedStudent.getName());
    }


    @Test
    @DisplayName("Delete removes student when successful")
    public void deleteRemoveStudentWhenSuccessful() {
        Student student = createStudent();

        Student savedStudent = this.studentRepository.save(student);

        this.studentRepository.delete(savedStudent);

        Optional<Student> studentDeleted = this.studentRepository.findById(savedStudent.getId());

        Assertions.assertThat(studentDeleted.isEmpty()).isTrue();
    }


    @Test
    @DisplayName("Find by name returns student when successful")
    public void findByNameReturnStudentWhenSuccessful() {
        Student student = createStudent();

        Student savedStudent = this.studentRepository.save(student);

        String name = savedStudent.getName();

        List<Student> listNames = this.studentRepository.findByName(name);

        Assertions.assertThat(listNames).isNotEmpty();
        Assertions.assertThat(listNames).contains(savedStudent);
    }


    @Test
    @DisplayName("Find by name returns empty list when not student is found")
    public void findByNameReturnEmptyListWhenNotFoundStudend() {
        String name = "not-found-name";

        List<Student> listNames = this.studentRepository.findByName(name);

        Assertions.assertThat(listNames).isEmpty();
    }


    @Test
    @DisplayName("Save throw exception when name is empty")
    public void saveThrowExceptionWhenNameIsEmpty() {
        Student student = new Student();

//        Assertions.assertThatThrownBy(() -> studentRepository.save(student))
//                .isInstanceOf(ConstraintViolationException.class);

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> studentRepository.save(student))
                .withMessageContaining("The name is required");
    }




    private Student createStudent() {
        return Student.builder()
                .name("User Test One")
                .build();
    }
}