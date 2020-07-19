package com.severo.demospring.exception;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class ExceptionDetails {

    protected String title;
    protected int status;
    protected String detail;
    protected LocalDateTime timeStamp;
    protected String developerMessage;
}
