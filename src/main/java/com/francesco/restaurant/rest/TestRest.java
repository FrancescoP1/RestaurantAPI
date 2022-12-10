package com.francesco.restaurant.rest;

import com.francesco.restaurant.model.RestaurantTable;
import com.francesco.restaurant.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

@RestController
public class TestRest {
    @Autowired
    private TableRepository tableRepository;

    @RequestMapping(path = "/saveTableTest")
    private int saveTableTest() {
        RestaurantTable restaurantTable = new RestaurantTable();
        restaurantTable.setStatus("FREE");
        restaurantTable.setCapacity(6);
        tableRepository.save(restaurantTable);
        return 0;
    }
}
