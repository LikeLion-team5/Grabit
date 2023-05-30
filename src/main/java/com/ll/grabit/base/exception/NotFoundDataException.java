package com.ll.grabit.base.exception;

public class NotFoundDataException extends RuntimeException{
    public NotFoundDataException() {
        super();
    }
    public NotFoundDataException(String errorMessage) {
        super(errorMessage);
    }
}
