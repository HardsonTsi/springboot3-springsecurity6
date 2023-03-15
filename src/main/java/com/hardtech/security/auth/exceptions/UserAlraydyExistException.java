package com.hardtech.security.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.IM_USED)
public class UserAlraydyExistException extends RuntimeException {
    public UserAlraydyExistException(String s) {
        super(s);
    }
}
