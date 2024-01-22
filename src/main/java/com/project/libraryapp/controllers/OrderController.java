package com.project.libraryapp.controllers;

import com.project.libraryapp.entities.Order;
import com.project.libraryapp.requests.OrderCreateRequest;
import com.project.libraryapp.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService){
        this.orderService=orderService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public List<Order> getAllOrders(@RequestParam Optional<Long> userId, @RequestParam Optional<Long> bookId){
        return orderService.getAllOrders(userId,bookId);
    }
    @GetMapping
    public ResponseEntity<?> getAuthAllOrders(@RequestParam Optional<Long> bookId){
        return orderService.getAuthAllOrders(bookId);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public void createOrder(@RequestBody OrderCreateRequest order){
        orderService.createOrder(order);
    }

    @PostMapping
    public ResponseEntity<?> createAuthOrder(@RequestParam String bookName){
        return orderService.createAuthOrder(bookName);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{orderId}")
    public Order getOneOrder(@PathVariable Long orderId){
        return orderService.getOneOrderById(orderId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{orderId}")
    public Order updateOrder(@PathVariable Long orderId,@RequestBody OrderCreateRequest updatedOrder){
        return orderService.updateOrderById(orderId,updatedOrder);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{orderId}")
    public void deleteOrder(@PathVariable Long orderId){
        orderService.deleteOrderById(orderId);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteAuthOrder(@PathVariable Long orderId){
        return orderService.deleteAuthOrderById(orderId);
    }
}
