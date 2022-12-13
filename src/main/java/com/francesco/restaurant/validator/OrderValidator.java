package com.francesco.restaurant.validator;

import com.francesco.restaurant.annotations.OrderConstraint;
import com.francesco.restaurant.constants.TableConstants;
import com.francesco.restaurant.model.MenuItem;
import com.francesco.restaurant.model.Order;
import com.francesco.restaurant.model.RestaurantTable;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OrderValidator implements ConstraintValidator<OrderConstraint, Order> {
    @Override
    public void initialize(OrderConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Order order, ConstraintValidatorContext constraintValidatorContext) {
        RestaurantTable table = order.getOrderTable();
        return table.getStatus() == TableConstants.AVAILABLE;
    }
}
