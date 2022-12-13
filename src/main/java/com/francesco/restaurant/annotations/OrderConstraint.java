package com.francesco.restaurant.annotations;

import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderConstraint {
    String message() default "Invalid order";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
