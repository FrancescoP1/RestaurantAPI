package com.francesco.restaurant.rest;

import com.francesco.restaurant.model.Order;
import com.francesco.restaurant.model.Response;
import com.francesco.restaurant.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/api/orders")
@RestController
public class OrderRestController {
    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET, path = "getOrderDetails")
    ResponseEntity<Response> getOrderDetails(@RequestParam(name = "orderId") int orderId) {
        Order orderToFind = orderService.findOrderById(orderId);
        HttpHeaders httpHeaders = new HttpHeaders();
        Response requestResponse = new Response();
        if(orderToFind != null) {
            httpHeaders.add("order-found", "true");
            requestResponse.setMessage("Order id " + orderId + " successfully found!");
            requestResponse.setResponseObject(orderToFind);
            return new ResponseEntity<>(requestResponse, httpHeaders, HttpStatus.ACCEPTED);
        } else {
            httpHeaders.add("order-found", "false");
            requestResponse.setMessage("Order id " + orderId + " not found!");
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.valueOf(404));
        }
    }

    @DeleteMapping(path = "deleteOrder")
    ResponseEntity<Response> deleteOrder(@RequestParam(name = "orderId") int orderId) {
        Order orderToDelete = orderService.deleteOrder(orderId);
        Response requestResponse = new Response();
        if(orderToDelete != null) {
            requestResponse.setMessage("Order id: "  + orderId + " deleted successfully");
            requestResponse.setResponseObject(orderToDelete);
            return ResponseEntity.accepted().
                    header("order-deleted", "true").
                    body(requestResponse);
        } else {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("order-deleted", "false");
            requestResponse.setMessage("Order id " + orderId + " not found!");
            return new ResponseEntity<>(requestResponse, httpHeaders, HttpStatus.valueOf(404));
        }
    }

    @PostMapping(path = "createNewOrder")
    ResponseEntity<Response> createNewOrder(@RequestBody Order order) {
        Order orderToCreate = orderService.createOrder(order);
        HttpHeaders httpHeaders = new HttpHeaders();
        Response requestResponse = new Response();
        if(orderToCreate != null && orderToCreate.getOrderId() > 0) {
            httpHeaders.add("order-created", "true");
            requestResponse.setMessage("Order successfully created!");
            requestResponse.setResponseObject(orderToCreate);
            return new ResponseEntity<>(requestResponse, httpHeaders, HttpStatus.OK);
        } else {
            httpHeaders.add("order-created", "false");
            requestResponse.setMessage("Order was not created, try again!");
            return new ResponseEntity<>(requestResponse, httpHeaders, HttpStatus.CONFLICT);
        }
    }

    @PutMapping(path = "removeItemFromOrder")
    ResponseEntity<Response> removeItemFromOrder(@RequestParam (name = "orderId") int orderId,
                                                 @RequestParam (name = "itemId") int itemId) {
        Response requestResponse = orderService.removeOrderItem(orderId, itemId);
        return new ResponseEntity<>(requestResponse, HttpStatus.valueOf(requestResponse.getStatusCode()));
    }

    @PutMapping(path = "addItemToOrder")
    ResponseEntity<Response> addItemToOrder(@RequestParam (name = "orderId") int orderId,
                                            @RequestParam (name = "itemId") int itemId) {
        Response requestResponse = orderService.addItemToOrder(orderId, itemId);
        return new ResponseEntity<>(requestResponse, HttpStatus.valueOf(requestResponse.getStatusCode()));
    }


}
