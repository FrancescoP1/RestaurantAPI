package com.francesco.restaurant.exception;

import com.francesco.restaurant.model.Response;

public class ObjectNotFoundException extends CustomExceptionBase {

    public ObjectNotFoundException(Response response) {
        this.setResponse(response);
    }
}
