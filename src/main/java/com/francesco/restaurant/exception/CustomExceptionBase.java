package com.francesco.restaurant.exception;

import com.francesco.restaurant.model.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomExceptionBase extends Exception{
    private Response response;
}
