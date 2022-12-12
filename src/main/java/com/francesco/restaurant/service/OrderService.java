package com.francesco.restaurant.service;

import com.francesco.restaurant.constants.TableConstants;
import com.francesco.restaurant.model.MenuItem;
import com.francesco.restaurant.model.Order;
import com.francesco.restaurant.model.Response;
import com.francesco.restaurant.repository.MenuItemRepository;
import com.francesco.restaurant.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;
    public Order deleteOrder(int orderId) {
        Optional<Order> orderToDelete = orderRepository.findById(orderId);
        if(orderToDelete.isPresent()) {
            if(orderToDelete.get().getOrderId() > 0) {
                orderRepository.delete(orderToDelete.get());
                return orderToDelete.get();
            }
        }
        return null;
    }

    public Order findOrderById(int orderId) {
        Optional<Order> orderToFind = orderRepository.findById(orderId);
        if(orderToFind.isPresent() && orderToFind.get().getOrderId() > 0) {
            return orderToFind.get();
        }
        return null;
    }

    public Order createOrder(Order order) {
        if(order != null && order.getOrderTable() != null) {
            order.getOrderTable().setStatus(TableConstants.RESERVED);
            return orderRepository.save(order);
        }
        return null;
    }

    public Response removeOrderItem(int orderId, int itemId) {
        Optional<Order> orderToModify = orderRepository.findById(orderId);
        Optional<MenuItem> itemToRemove = menuItemRepository.findById(itemId);
        Response response = new Response();
        if(orderToModify.isPresent() && orderToModify.get().getOrderId() > 0) {
            Order order = orderToModify.get();
            if(itemToRemove.isEmpty()) {
                response.setMessage("Menu item does not exist!");
                response.setStatusCode(404);
            } else if(itemToRemove.get().getItemId() > 0 && order.getOrderItems().remove(itemToRemove.get())) {
                order = orderRepository.save(order);
                response.setResponseObject(order);
                response.setMessage("Item id: " + itemId + " successfully removed from order!");
                response.setStatusCode(200);
            } else {
                response.setMessage("Menu item id: " + itemId + " not present in order id: " + orderId);
                response.setStatusCode(404);
            }
        } else {
            response.setMessage("Order id: " + orderId + " not found!");
            response.setStatusCode(404);
        }
        return response;
    }

    public Response addItemToOrder(int orderId, int itemId) {
        Optional<Order> orderToModify = orderRepository.findById(orderId);
        Optional<MenuItem> itemToAdd = menuItemRepository.findById(itemId);
        Response response = new Response();
        response.setStatusCode(404);
        if(!(orderToModify.isPresent() && orderToModify.get().getOrderId() > 0)) {
            response.setMessage("Order id: " + orderId + " not found!");
        } else if(!(itemToAdd.isPresent() && itemToAdd.get().getItemId() > 0)) {
            response.setMessage("Item id: " + itemId + " not found!");
        } else {
            orderToModify.get().getOrderItems().add(itemToAdd.get());
            orderRepository.save(orderToModify.get());
            response.setMessage("Item id: " + itemId + " successfully added to order id: " + orderId + "!");
            response.setStatusCode(200);
        }
        return response;
    }
}
