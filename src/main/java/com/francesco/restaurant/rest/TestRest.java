package com.francesco.restaurant.rest;

import com.francesco.restaurant.constants.TableConstants;
import com.francesco.restaurant.model.MenuItem;
import com.francesco.restaurant.model.Order;
import com.francesco.restaurant.model.RestaurantTable;
import com.francesco.restaurant.repository.MenuItemRepository;
import com.francesco.restaurant.repository.OrderRepository;
import com.francesco.restaurant.repository.TableRepository;
import com.francesco.restaurant.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@RestController
public class TestRest {
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @RequestMapping(path = "/saveTableTest")
    private int saveTableTest() {
        RestaurantTable restaurantTable = new RestaurantTable();
        restaurantTable.setStatus(TableConstants.AVAILABLE);
        restaurantTable.setCapacity(9);
        tableRepository.save(restaurantTable);
        return 0;
    }

    @RequestMapping(path = "populateData")
    private int populateData() {
        // add 10 menu items and 10 tables
        List<MenuItem> itemsAdded = new ArrayList<>();
        List<RestaurantTable> tablesAdded = new ArrayList<>();
        for(int i = 0; i < 10; ++i) {
            MenuItem itemToAdd = new MenuItem();
            itemToAdd.setName("item" + i);
            itemToAdd.setDescription("item added through /populateData");
            itemToAdd = menuItemRepository.save(itemToAdd);
            itemsAdded.add(itemToAdd);
            RestaurantTable tableToAdd = new RestaurantTable();
            tableToAdd.setStatus(TableConstants.AVAILABLE);
            tableToAdd.setCapacity(new Random().nextInt(2, 10));
            tableToAdd = tableRepository.save(tableToAdd);
            System.out.println(tableToAdd.getTableId());
            tablesAdded.add(tableToAdd);
        }
        //create 3 valid orders
        for(int i = 0; i < 3; ++i) {
            Order orderToCreate = new Order();
            orderToCreate = orderRepository.save(orderToCreate);
            orderToCreate.setOrderTable(tablesAdded.get(i));
            orderToCreate.getOrderItems().add(itemsAdded.get(i));
            for(MenuItem item : orderToCreate.getOrderItems()) {
                System.out.println(item.getDescription());
            }
            orderRepository.save(orderToCreate);
        }

        return 0;
    }

}
