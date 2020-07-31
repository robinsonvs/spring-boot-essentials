package com.severo.demospring.integration;

import com.severo.demospring.domain.Student;
import com.severo.demospring.repository.StudentRepository;
import com.severo.demospring.util.StudentCreator;
import com.severo.demospring.util.wrapper.PageableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerIT {

    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateUser;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateAdmin;

    @MockBean
    private StudentRepository studentRepositoryMock;

    @Lazy
    @TestConfiguration
    static class Config {

        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("severo", "demospring");
            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:" + port)
                    .basicAuthentication("robinson", "demospring");
            return new TestRestTemplate(restTemplateBuilder);
        }
    }

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
    }

    @Test
    @DisplayName("listAll returns a pageable list of students when ok")
    public void listAllReturnListOfStudentsInsidePageObjectWhenOK() {
        String expectedName = StudentCreator.createValidStudent().getName();

        //@formatter:off
        Page<Student> studentPage = testRestTemplateUser.exchange("/students", HttpMethod.GET,
                null, new ParameterizedTypeReference<PageableResponse<Student>>() {}).getBody();
        //@formatter:on

        assertThat(studentPage).isNotNull();
        assertThat(studentPage.toList()).isNotEmpty();
        assertThat(studentPage.toList().get(0).getName()).isEqualTo(expectedName);
    }


    @Test
    @DisplayName("findById returns an student when ok")
    public void findByIdReturnListOfStudentsInsidePageObjectWhenOK() {
        Integer expectedById = StudentCreator.createValidStudent().getId();

        Student student = testRestTemplateUser.getForObject("/students/1", Student.class);

        assertThat(student).isNotNull();
        assertThat(student.getId()).isNotNull();
        assertThat(student.getId()).isEqualTo(expectedById);
    }


    @Test
    @DisplayName("findByName returns a list of students when ok")
    public void findByNameReturnListOfStudentsInsidePageObjectWhenOK() {
        String expectedName = StudentCreator.createValidStudent().getName();

        //@formatter:off
        List<Student> studentList = testRestTemplateUser.exchange("/students/find?name='ABC'",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
                }).getBody();
        //@formatter:on

        assertThat(studentList).isNotNull();
        assertThat(studentList).isNotEmpty();
        assertThat(studentList.get(0).getName()).isEqualTo(expectedName);
    }


    @Test
    @DisplayName("save creates returns an student when ok")
    public void saveCreatesAnStudentWhenOK() {
        Integer expectedById = StudentCreator.createValidStudent().getId();

        Student studentToBeSaved = StudentCreator.createStudentToBeSaved();
        Student student = testRestTemplateUser.exchange("/students", HttpMethod.POST,
                createJsonHttpEntity(studentToBeSaved), Student.class).getBody();

        assertThat(student).isNotNull();
        assertThat(student.getId()).isNotNull();
        assertThat(student.getId()).isEqualTo(expectedById);
    }

    @Test
    @DisplayName("delete removes the student when ok")
    public void deleteRemovesAnStudentWhenOK() {
        ResponseEntity<Void> responseEntity = testRestTemplateAdmin.exchange("/students/admin/1", HttpMethod.DELETE,
                null, Void.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }


    @Test
    @DisplayName("delete returns forbidden when user does not have the role admin")
    public void deleteReturna403WhenUserIsNotAdmin() {
        ResponseEntity<Void> responseEntity = testRestTemplateUser.exchange("/students/admin/1", HttpMethod.DELETE,
                null, Void.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(responseEntity.getBody()).isNull();
    }


    @Test
    @DisplayName("update save updated student when ok")
    public void updateSaveUpdatedStudentWhenOK() {
        Student validStudent = StudentCreator.createValidStudent();

        ResponseEntity<Void> responseEntity = testRestTemplateUser.exchange("/students", HttpMethod.PUT,
                createJsonHttpEntity(validStudent), Void.class);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(responseEntity.getBody()).isNull();
    }


    private HttpEntity<Student> createJsonHttpEntity(Student student) {
        return new HttpEntity<>(student, createJsonHeader());
    }


    private static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}