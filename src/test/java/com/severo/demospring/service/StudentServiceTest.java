package com.severo.demospring.service;

import com.severo.demospring.domain.Student;
import com.severo.demospring.exception.ResourceNotFoundException;
import com.severo.demospring.repository.StudentRepository;
import com.severo.demospring.util.StudentCreator;
import com.severo.demospring.util.Utils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepositoryMock;

    @Mock
    private Utils utilsMock;

    @BeforeEach
    public void setup() {
        PageImpl<Student> studentPage = new PageImpl<>(List.of(StudentCreator.createValidStudent()));
        when(studentRepositoryMock.findAll(any(PageRequest.class)))
                .thenReturn(studentPage);

        when(studentRepositoryMock.findById(anyInt()))
                .thenReturn(Optional.of(StudentCreator.createValidStudent()));

        when(studentRepositoryMock.findByName(anyString()))
                .thenReturn(List.of(StudentCreator.createValidStudent()));

        when(studentRepositoryMock.save(StudentCreator.createStudentToBeSaved()))
                .thenReturn(StudentCreator.createValidStudent());

        doNothing().when(studentRepositoryMock).delete(any(Student.class));

        when(studentRepositoryMock.save(StudentCreator.createValidStudent()))
                .thenReturn(StudentCreator.createValidUpdatedStudent());

        when(utilsMock.findStudentOrThrowNotFound(anyInt(), any(StudentRepository.class)))
                .thenReturn(StudentCreator.createValidStudent());
    }

    @Test
    @DisplayName("listAll returns a pageable list of students when ok")
    public void listAllReturnListOfStudentsInsidePageObjectWhenOK() {
        String expectedName = StudentCreator.createValidStudent().getName();

        Page<Student> studentPage = studentService.listAll(PageRequest.of(1, 1));

        assertThat(studentPage).isNotNull();
        assertThat(studentPage.toList()).isNotEmpty();
        assertThat(studentPage.toList().get(0).getName()).isEqualTo(expectedName);
    }


    @Test
    @DisplayName("findById returns an student when ok")
    public void findByIdReturnListOfStudentsInsidePageObjectWhenOK() {
        Integer expectedById = StudentCreator.createValidStudent().getId();

        Student student = studentService.findById(1);

        assertThat(student).isNotNull();
        assertThat(student.getId()).isNotNull();
        assertThat(student.getId()).isEqualTo(expectedById);
    }


    @Test
    @DisplayName("findByName returns a pageable list of students when ok")
    public void findByNameReturnListOfStudentsInsidePageObjectWhenOK() {
        String expectedName = StudentCreator.createValidStudent().getName();

        List<Student> studentList = studentService.findByName("ABC");

        assertThat(studentList).isNotNull();
        assertThat(studentList).isNotEmpty();
        assertThat(studentList.get(0).getName()).isEqualTo(expectedName);
    }


    @Test
    @DisplayName("save creates returns an student when ok")
    public void saveCreatesAnStudentWhenOK() {
        Integer expectedById = StudentCreator.createValidStudent().getId();

        Student studentToBeSaved = StudentCreator.createStudentToBeSaved();
        Student student = studentService.save(studentToBeSaved);

        assertThat(student).isNotNull();
        assertThat(student.getId()).isNotNull();
        assertThat(student.getId()).isEqualTo(expectedById);
    }

    @Test
    @DisplayName("delete removes the student when ok")
    public void deleteRemovesAnStudentWhenOK() {
        Assertions.assertThatCode(() -> studentService.delete(1))
            .doesNotThrowAnyException();
    }


    @Test
    @DisplayName("delete throw ResourceNotFoundException when the student does not exist")
    public void deleteThrowResourceNotFoundExceptionWhenStudentNotExist() {
        when(utilsMock.findStudentOrThrowNotFound(anyInt(), any(StudentRepository.class)))
                .thenThrow(new ResourceNotFoundException("Student not found"));

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> studentService.delete(1));
    }

    @Test
    @DisplayName("save updating update student when ok")
    public void saveSaveUpdatedStudentWhenOK() {
        Student validUpdatedStudent = StudentCreator.createValidUpdatedStudent();

        String expectedName = validUpdatedStudent.getName();

        Student student = studentService.save(StudentCreator.createValidStudent());

        assertThat(student).isNotNull();
        assertThat(student.getId()).isNotNull();
        assertThat(student.getName()).isEqualTo(expectedName);
    }
}