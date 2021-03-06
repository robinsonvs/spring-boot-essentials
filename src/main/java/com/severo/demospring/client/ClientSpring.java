package com.severo.demospring.client;

import com.severo.demospring.domain.Student;
import com.severo.demospring.util.wrapper.PageableResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class ClientSpring {

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("demospring"));

        //testGetWithRestTemplate();

        //@formatter:off
        ResponseEntity<PageableResponse<Student>> exchangeStudentList = new RestTemplate()
                .exchange("http://localhost:8080/students?sort=name,desc", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Student>>() {
                });
        //@formatter:on

        log.info("Student list {}", exchangeStudentList.getBody());

        Student newStudentPost = Student.builder().name("newStudentPost").build();
//        Student newStudendPostSaved = new RestTemplate().postForObject("http://localhost:8080/students", newStudentPost, Student.class);
//        log.info("New Studend Saved id {}", newStudendPostSaved.getId());

        Student newStudentExchange = Student.builder().name("newStudentExchange").build();
        Student newStudentExchangeSaved = new RestTemplate()
                .exchange("http://localhost:8080/students", HttpMethod.POST, new HttpEntity<>(newStudentExchange, createJsonHeader()), Student.class)
                .getBody();
        log.info("New Student Exchange Saved id:d {}", newStudentExchangeSaved.getId());


        newStudentExchangeSaved.setName("Student updated");
        ResponseEntity<Void> updatedExchangeStudent = new RestTemplate()
                .exchange("http://localhost:8080/students", HttpMethod.PUT,
                        new HttpEntity<>(newStudentExchangeSaved, createJsonHeader()), Void.class);
        log.info("New Student Exchange Updated status {}", updatedExchangeStudent.getStatusCode());


        newStudentExchangeSaved.setName("Student deleted");
        ResponseEntity<Void> deletedExchangeStudent = new RestTemplate()
                .exchange("http://localhost:8080/students/{id}", HttpMethod.DELETE,
                        null, Void.class, newStudentExchangeSaved.getId());
        log.info("New Student Exchange Deleted status {}", deletedExchangeStudent.getStatusCode());
    }

    private static void testGetWithRestTemplate() {
        ResponseEntity<Student> studentResponseEntity = new RestTemplate()
                .getForEntity("http://localhost:8080/students/{id}", Student.class, 1);

        log.info("Response entity {}", studentResponseEntity);

        log.info("Response Data {}", studentResponseEntity.getBody());

        Student student = new RestTemplate()
                .getForObject("http://localhost:8080/students/{id}", Student.class, 1);

        log.info("Student {}", student);
    }

    private static HttpHeaders createJsonHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
