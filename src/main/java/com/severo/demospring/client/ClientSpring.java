package com.severo.demospring.client;

import com.severo.demospring.domain.Student;
import com.severo.demospring.util.wrapper.PageableResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class ClientSpring {

    public static void main(String[] args) {

        ResponseEntity<Student> studentResponseEntity = new RestTemplate()
                .getForEntity("http://localhost:8080/students/{id}", Student.class, 1);

        log.info("Response entity {}", studentResponseEntity);

        log.info("Response Data {}", studentResponseEntity.getBody());

        Student student = new RestTemplate()
                .getForObject("http://localhost:8080/students/{id}", Student.class, 1);

        log.info("Student {}", student);

//        Student[] studentArray = new RestTemplate()
//                .getForObject("http://localhost:8080/students", Student[].class);
//
//        log.info("Student array {}", Arrays.toString(studentArray));
//
//        //@formatter:off
//        ResponseEntity<List<Student>> exchangeStudentList = new RestTemplate()
//                .exchange("http://localhost:8080/students", HttpMethod.GET, null, new ParameterizedTypeReference<>() {
//                });
//        //@formatter:on
//
//        log.info("Student list {}", exchangeStudentList.getBody());

        //@formatter:off
        ResponseEntity<PageableResponse<Student>> exchangeStudentList = new RestTemplate()
                .exchange("http://localhost:8080/students?sort=name,desc", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Student>>() {
                });
        //@formatter:on

        log.info("Student list {}", exchangeStudentList.getBody());
    }
}
