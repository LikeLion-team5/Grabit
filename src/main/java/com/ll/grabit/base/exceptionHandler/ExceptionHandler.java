package com.ll.grabit.base.exceptionHandler;

import com.ll.grabit.base.exception.NotFoundDataException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundDataException.class)
    public String DataNotFoundException(NotFoundDataException e) {
        return "error/not_found";
    }
}
