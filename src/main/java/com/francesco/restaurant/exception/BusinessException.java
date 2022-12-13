package com.francesco.restaurant.exception;


import com.francesco.restaurant.model.Response;

public class BusinessException extends CustomExceptionBase{

    public BusinessException(Response response) {
        this.setResponse(response);
    }
}
