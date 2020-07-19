package com.severo.demospring.client;

import com.severo.demospring.domain.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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
    }
}
