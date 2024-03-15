package com.rucha.PasswordManager.vo;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Data
public class ResponseStatus {
    private HttpStatusCode responseStatusCode;
    private String responseStatusMsg;
    private Object object;

    public ResponseStatus(HttpStatus httpStatus, String message) {
        this.responseStatusCode = httpStatus;
        this.responseStatusMsg = message;
    }

    public ResponseStatus(HttpStatus httpStatus, String message, Object object) {
        this.responseStatusCode = httpStatus;
        this.responseStatusMsg = message;
        this.object = object;
    }
}
