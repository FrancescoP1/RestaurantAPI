package com.francesco.restaurant.rest;

import com.francesco.restaurant.exception.BusinessException;
import com.francesco.restaurant.exception.ObjectNotFoundException;
import com.francesco.restaurant.model.Order;
import com.francesco.restaurant.model.Response;
import com.francesco.restaurant.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping(path = "/api/orders")
@RestController
public class OrderRestController {
    @Autowired
    private OrderService orderService;

    @RequestMapping(method = RequestMethod.GET, path = "getOrderDetails")
    ResponseEntity<Response> getOrderDetails(@RequestParam(name = "orderId") int orderId) throws ObjectNotFoundException {
        Order orderToFind = orderService.findOrderById(orderId);
        HttpHeaders httpHeaders = new HttpHeaders();
        Response requestResponse = new Response();
        httpHeaders.add("order-found", "true");
        requestResponse.setMessage("Order id " + orderId + " successfully found!");
        requestResponse.setResponseObject(orderToFind);
        return new ResponseEntity<>(requestResponse, httpHeaders, HttpStatus.ACCEPTED);

    }

    @DeleteMapping(path = "deleteOrder")
    ResponseEntity<Response> deleteOrder(@RequestParam(name = "orderId") int orderId) throws ObjectNotFoundException {
        Order orderToDelete = orderService.deleteOrder(orderId);
        Response requestResponse = new Response();
        requestResponse.setMessage("Order id: "  + orderId + " deleted successfully");
        requestResponse.setResponseObject(orderToDelete);
        return ResponseEntity.accepted().
                header("order-deleted", "true").
                body(requestResponse);
    }

    @PostMapping(path = "createNewOrder")
    ResponseEntity<Response> createNewOrder(@Valid @RequestBody Order order, BindingResult errors) throws BusinessException {
        if(errors.hasErrors()) {
            throw new BusinessException(new Response("Table is reserved!", 400, null));
        }
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
                                                 @RequestParam (name = "itemId") int itemId) throws ObjectNotFoundException {
        Response requestResponse = orderService.removeOrderItem(orderId, itemId);
        return new ResponseEntity<>(requestResponse, HttpStatus.valueOf(requestResponse.getStatusCode()));
    }

    @PutMapping(path = "addItemToOrder")
    ResponseEntity<Response> addItemToOrder(@RequestParam (name = "orderId") int orderId,
                                            @RequestParam (name = "itemId") int itemId) throws ObjectNotFoundException {
        Response requestResponse = orderService.addItemToOrder(orderId, itemId);
        return new ResponseEntity<>(requestResponse, HttpStatus.valueOf(requestResponse.getStatusCode()));
    }


}
