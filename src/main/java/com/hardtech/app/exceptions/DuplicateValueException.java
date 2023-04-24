package com.hardtech.app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateValueException extends RuntimeException{
    public DuplicateValueException(String message){
        super(message);
    }

    public DuplicateValueException(String message, Throwable cause){
        super(message, cause);
    }
}
