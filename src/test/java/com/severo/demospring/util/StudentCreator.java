package com.severo.demospring.util;

import com.severo.demospring.domain.Student;

public class StudentCreator {

    public static Student createStudentToBeSaved() {
        return Student.builder()
                .name("User Test One Saved")
                .build();
    }

    public static Student createValidStudent() {
        return Student.builder()
                .id(1)
                .name("User Test One")
                .build();
    }

    public static Student createValidUpdatedStudent() {
        return Student.builder()
                .id(1)
                .name("User Test Two Updated")
                .build();
    }
}
