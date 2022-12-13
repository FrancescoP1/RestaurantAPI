package com.francesco.restaurant.exception;

import com.francesco.restaurant.model.Response;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(annotations = RestController.class)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomExceptionBase.class)
    public ResponseEntity<Response> exceptionHandler(CustomExceptionBase exception) {
        Response response = exception.getResponse();
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatusCode()));
    }

}
