package com.francesco.restaurant.service;

import com.francesco.restaurant.constants.TableConstants;
import com.francesco.restaurant.exception.BusinessException;
import com.francesco.restaurant.exception.ObjectNotFoundException;
import com.francesco.restaurant.model.MenuItem;
import com.francesco.restaurant.model.Order;
import com.francesco.restaurant.model.Response;
import com.francesco.restaurant.model.RestaurantTable;
import com.francesco.restaurant.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    @Autowired
    private MenuItemService itemService;
    @Autowired
    private RestaurantTableService tableService;
    public Order deleteOrder(int orderId) throws ObjectNotFoundException{
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
            restaurantTable.setStatus(TableConstants.RESERVED);
            Order orderToReturn = new Order();
            orderToReturn.setOrderItems(orderItems);
            orderToReturn.setOrderTable(restaurantTable);
            orderToReturn = orderRepository.save(orderToReturn);
            for(MenuItem item : orderItems) {
                item.getOrders().add(orderToReturn);
            }
            itemService.getMenuItemRepository().saveAll(orderItems);
            tableService.getTableRepository().save(restaurantTable);
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
            throw new ObjectNotFoundException(response);
        }
        orderRepository.save(orderToModify);
        response.setMessage("Item id: " + itemId + " successfully removed from order id: " + orderId);
        response.setStatusCode(200);
        return response;
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
