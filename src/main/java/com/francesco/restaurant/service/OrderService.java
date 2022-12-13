package com.francesco.restaurant.service;

import com.francesco.restaurant.constants.TableConstants;
import com.francesco.restaurant.exception.ObjectNotFoundException;
import com.francesco.restaurant.model.MenuItem;
import com.francesco.restaurant.model.Order;
import com.francesco.restaurant.model.Response;
import com.francesco.restaurant.model.RestaurantTable;
import com.francesco.restaurant.repository.MenuItemRepository;
import com.francesco.restaurant.repository.OrderRepository;
import com.francesco.restaurant.repository.TableRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.ObjectDeletedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Optional;
import java.util.Set;

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

    @Autowired
    private TableRepository tableRepository;
    public Order deleteOrder(int orderId) throws ObjectNotFoundException{
        Optional<Order> orderToDelete = orderRepository.findById(orderId);
        if(orderToDelete.isPresent()) {
            if(orderToDelete.get().getOrderId() > 0) {
                orderRepository.delete(orderToDelete.get());
                return orderToDelete.get();
            } else {
                Response response = new Response();
                response.setStatusCode(404);
                response.setMessage("Order id: " + " not found!");
                throw new ObjectNotFoundException(response);
            }
        }
        return null;
    }

    public Order findOrderById(int orderId) throws ObjectNotFoundException{
        Optional<Order> orderToFind = orderRepository.findById(orderId);
        if(orderToFind.isPresent() && orderToFind.get().getOrderId() > 0) {
            return orderToFind.get();
        } else {
            Response response = new Response();
            response.setMessage("Order id: " + orderId + " not found!");
            response.setStatusCode(404);
            throw new ObjectNotFoundException(response);
        }
    }

    public Order createOrder(Order order) {
        if(order != null && order.getOrderTable() != null) {
            order.getOrderTable().setStatus(TableConstants.RESERVED);
            return orderRepository.save(order);
        }
        return null;
    }

    public Response removeOrderItem(int orderId, int itemId) throws ObjectNotFoundException{
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
        if(response.getStatusCode() != 200) {
            throw new ObjectNotFoundException(response);
        }
        return response;
    }

    public Response addItemToOrder(int orderId, int itemId) throws ObjectNotFoundException{
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
        if(response.getStatusCode() != 200) {
            throw new ObjectNotFoundException(response);
        }
        return response;
    }

    public Response isTableValid(RestaurantTable restaurantTable) {
        Response response = new Response();
        response.setStatusCode(404);
        if(restaurantTable != null) {
            Optional<RestaurantTable> tableToCheck = tableRepository.findById(restaurantTable.getTableId());
            if(tableToCheck.isPresent() && tableToCheck.get().getTableId() > 0){
                if(tableToCheck.get().getStatus().equals(TableConstants.AVAILABLE)) {
                    response.setMessage("Table is available");
                    response.setStatusCode(200);
                } else {
                    response.setMessage("Table is already reserved!");
                    response.setStatusCode(400);
                }
            } else {
                response.setMessage("Invalid Table");
            }
        } else {
            response.setMessage("No table present in order!");
        }
        return response;
    }
}
