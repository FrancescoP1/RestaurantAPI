package com.francesco.restaurant.rest;

import com.francesco.restaurant.constants.TableConstants;
import com.francesco.restaurant.model.Order;
import com.francesco.restaurant.model.RestaurantTable;
import com.francesco.restaurant.repository.TableRepository;
import com.francesco.restaurant.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;

@RestController
public class TestRest {
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private OrderService orderService;

    @RequestMapping(path = "/saveTableTest")
    private int saveTableTest() {
        RestaurantTable restaurantTable = new RestaurantTable();
        restaurantTable.setStatus(TableConstants.AVAILABLE);
        restaurantTable.setCapacity(9);
        tableRepository.save(restaurantTable);
        return 0;
    }

}
