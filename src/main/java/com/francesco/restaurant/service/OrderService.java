package com.francesco.restaurant.service;

import com.francesco.restaurant.constants.TableConstants;
import com.francesco.restaurant.exception.BusinessException;
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
import java.util.HashSet;
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

    /*
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private TableRepository tableRepository;
     */

    @Autowired
    private MenuItemService itemService;

    @Autowired
    private RestaurantTableService tableService;
    public Order deleteOrder(int orderId) throws ObjectNotFoundException{
        /*
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
         */
        Order orderToDelete = this.validateOrder(orderId);
        RestaurantTable orderTable = orderToDelete.getOrderTable();
        orderTable.getOrders().remove(orderToDelete);
        tableService.getTableRepository().save(orderTable);
        for(MenuItem item : orderToDelete.getOrderItems()) {
            item.getOrders().remove(orderToDelete);
            itemService.getMenuItemRepository().save(item);
        }
        orderToDelete.setOrderItems(null);
        orderRepository.save(orderToDelete);
        orderRepository.delete(orderToDelete);
        return orderToDelete;
    }

    public Order findOrderById(int orderId) throws ObjectNotFoundException{
        /*
        Optional<Order> orderToFind = orderRepository.findById(orderId);
        if(orderToFind.isPresent() && orderToFind.get().getOrderId() > 0) {
            return orderToFind.get();
        } else {
            Response response = new Response();
            response.setMessage("Order id: " + orderId + " not found!");
            response.setStatusCode(404);
            throw new ObjectNotFoundException(response);
        }
         */
        return this.validateOrder(orderId);
    }

    public Order createOrder(Order order) throws ObjectNotFoundException, BusinessException {
        Response response = new Response();
        if(order != null && order.getOrderTable() != null) {
            RestaurantTable restaurantTable = tableService.validateTable(order.getOrderTable().getTableId());
            Set<MenuItem> orderItems = new HashSet<>();
            if(order.getOrderItems().size() > 0) {
                for(MenuItem item : order.getOrderItems()) {
                    orderItems.add(itemService.validateItem(item.getItemId()));
                }
            } else {
                response.setStatusCode(400);
                response.setMessage("Invalid order items");
                throw new BusinessException(response);
            }
            Order orderToReturn = new Order();
            orderToReturn.setOrderItems(orderItems);
            orderToReturn.setOrderTable(restaurantTable);
            return orderToReturn;
        }
        response.setStatusCode(400);
        response.setMessage("Invalid request format!");
        throw new BusinessException(response);
    }

    public Response removeOrderItem(int orderId, int itemId) throws ObjectNotFoundException{
        Order orderToModify = this.validateOrder(orderId);
        MenuItem itemToRemove = itemService.validateItem(itemId);
        Response response = new Response();
        if(!orderToModify.getOrderItems().remove(itemToRemove)) {
            response.setStatusCode(404);
            response.setMessage("Item id: " + itemId + " is not part of order id: " + orderId);
            return response;
        }
        orderRepository.save(orderToModify);
        response.setMessage("Item id: " + itemId + " successfully removed from order id: " + orderId);
        response.setStatusCode(200);
        return response;
        /*
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
         */
    }

    public Response addItemToOrder(int orderId, int itemId) throws ObjectNotFoundException{
        Order orderToModify = this.validateOrder(orderId);
        MenuItem itemToAdd = itemService.validateItem(itemId);
        orderToModify.getOrderItems().add(itemToAdd);
        orderRepository.save(orderToModify);
        Response response = new Response();
        response.setStatusCode(200);
        response.setMessage("Item id: " + itemId + " successfully added to order id: " + orderId);
        return response;
        /*
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
         */
    }

    public Order validateOrder(int orderId) throws ObjectNotFoundException {
        Optional<Order> orderToValidate = orderRepository.findById(orderId);
        if(!(orderToValidate.isPresent() && orderToValidate.get().getOrderId() > 0)) {
            Response response = new Response();
            response.setStatusCode(404);
            response.setMessage("Order id: " + orderId + " not found!");
            throw new ObjectNotFoundException(response);
        }
        return orderToValidate.get();
    }

}
