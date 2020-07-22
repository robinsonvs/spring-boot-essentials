package com.severo.demospring.controller;

import com.severo.demospring.domain.Student;
import com.severo.demospring.service.StudentService;
import com.severo.demospring.util.StudentCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.doNothing;

@ExtendWith(SpringExtension.class)
class StudentControllerTest {

    @InjectMocks
    private StudentController studentController;

    @Mock
    private StudentService studentServiceMock;

    @BeforeEach
    public void setup() {
        PageImpl<Student> studentPage = new PageImpl<>(List.of(StudentCreator.createValidStudent()));
        when(studentServiceMock.listAll(any()))
                .thenReturn(studentPage);

        when(studentServiceMock.findById(anyInt()))
                .thenReturn(StudentCreator.createValidStudent());

        when(studentServiceMock.findByName(anyString()))
                .thenReturn(List.of(StudentCreator.createValidStudent()));

        when(studentServiceMock.save(StudentCreator.createStudentToBeSaved()))
                .thenReturn(StudentCreator.createValidStudent());

        doNothing().when(studentServiceMock).delete(anyInt());

        when(studentServiceMock.save(StudentCreator.createValidStudent()))
                .thenReturn(StudentCreator.createValidUpdatedStudent());
    }

    @Test
    @DisplayName("listAll returns a pageable list of students when ok")
    public void listAllReturnListOfStudentsInsidePageObjectWhenOK() {
        String expectedName = StudentCreator.createValidStudent().getName();

        Page<Student> studentPage = studentController.listAll(null).getBody();

        assertThat(studentPage).isNotNull();
        assertThat(studentPage.toList()).isNotEmpty();
        assertThat(studentPage.toList().get(0).getName()).isEqualTo(expectedName);
    }


    @Test
    @DisplayName("findById returns an student when ok")
    public void findByIdReturnListOfStudentsInsidePageObjectWhenOK() {
        Integer expectedById = StudentCreator.createValidStudent().getId();

        Student student = studentController.findById(1).getBody();

        assertThat(student).isNotNull();
        assertThat(student.getId()).isNotNull();
        assertThat(student.getId()).isEqualTo(expectedById);
    }


    @Test
    @DisplayName("findByName returns a pageable list of students when ok")
    public void findByNameReturnListOfStudentsInsidePageObjectWhenOK() {
        String expectedName = StudentCreator.createValidStudent().getName();

        List<Student> studentList = studentController.findByName("ABC").getBody();

        assertThat(studentList).isNotNull();
        assertThat(studentList).isNotEmpty();
        assertThat(studentList.get(0).getName()).isEqualTo(expectedName);
    }


    @Test
    @DisplayName("save creates returns an student when ok")
    public void saveCreatesAnStudentWhenOK() {
        Integer expectedById = StudentCreator.createValidStudent().getId();

        Student studentToBeSaved = StudentCreator.createStudentToBeSaved();
        Student student = studentController.save(studentToBeSaved).getBody();

        assertThat(student).isNotNull();
        assertThat(student.getId()).isNotNull();
        assertThat(student.getId()).isEqualTo(expectedById);
    }

    @Test
    @DisplayName("delete removes the student when ok")
    public void deleteRemovesAnStudentWhenOK() {
        ResponseEntity<Void> responseEntity = studentController.delete(1);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(responseEntity.getBody()).isNull();
    }


    @Test
    @DisplayName("update save updated student when ok")
    public void updateSaveUpdatedStudentWhenOK() {
        Student validUpdatedStudent = StudentCreator.createValidUpdatedStudent();

        String expectedName = validUpdatedStudent.getName();

        Student student = studentController.save(StudentCreator.createValidStudent()).getBody();

        assertThat(student).isNotNull();
        assertThat(student.getId()).isNotNull();
        assertThat(student.getName()).isEqualTo(expectedName);
    }
}