package com.project.libraryapp.services;

import com.project.libraryapp.entities.Book;
import com.project.libraryapp.entities.Order;
import com.project.libraryapp.entities.User;
import com.project.libraryapp.repositories.OrderRepository;
import com.project.libraryapp.requests.OrderCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private UserService userService;
    private BookService bookService;
    @Autowired
    public OrderService(OrderRepository orderRepository,BookService bookService, UserService userService){
        this.orderRepository=orderRepository;
        this.bookService=bookService;
        this.userService=userService;
    }


    public List<Order> getAllOrders(Optional<Long> userId, Optional<Long> bookId) {
        if (userId.isPresent()&&bookId.isPresent()){
            return orderRepository.findByUser_IdAndBooks_Id(userId.get(),bookId.get());
        } else if (userId.isPresent()) {
            return orderRepository.findByUserId(userId.get());
        } else if (bookId.isPresent()) {
            return orderRepository.findByBooks_Id(bookId.get());
        }else {
            return orderRepository.findAll();
        }
    }

    public Order getOneOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public ResponseEntity<String> createOrder(OrderCreateRequest order) {
        User user=userService.getOneUser(order.getUserId());
        Book book=bookService.getOneBook(order.getBookId());
        if (user!=null && book!=null){
            Order toSaveOrder= new Order();
            toSaveOrder.setId(order.getId());
            toSaveOrder.setBooks(Collections.singletonList(book));
            toSaveOrder.setUser(user);
            orderRepository.save(toSaveOrder) ;
            return new ResponseEntity<>("The book has been created", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Sth went wrong",HttpStatus.BAD_REQUEST);
        }
    }

    public Order updateOrderById(Long orderId, OrderCreateRequest newOrder) {
        Optional<Order> order=orderRepository.findById(orderId);
        User user=userService.getOneUser(newOrder.getUserId());
        Book book=bookService.getOneBook(newOrder.getBookId());
        if (order.isPresent()){
            Order updatedOrder=order.get();
            updatedOrder.setId(newOrder.getId());
            updatedOrder.setUser(user);
            updatedOrder.setBooks(Collections.singletonList(book));
            return orderRepository.save(updatedOrder);
        }else {
            return null;
        }
    }
    public void deleteOrderById(long orderId) {
        orderRepository.deleteById(orderId);
    }

    public ResponseEntity<?> getAuthAllOrders(Optional<Long> bookId) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        User user=userService.findByUsername(username);
        if (authentication.isAuthenticated()&& bookId.isPresent()){
            List<Order>orders= orderRepository.findByUser_IdAndBooks_Id(user.getId(),bookId.get());
            return ResponseEntity.ok(orders);
        } else if (authentication.isAuthenticated()) {
            List<Order>orders= orderRepository.findByUserId(user.getId());
            List<Book> books = orders.stream()
                    .flatMap(order -> order.getBooks().stream())
                    .toList();
            return ResponseEntity.ok(books);
        }
        else
            return new ResponseEntity<>("Orders not found!",HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> createAuthOrder(String bookName) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        User user=userService.findByUsername(username);
        if (authentication.isAuthenticated()&& bookName!=null){
            Book book=bookService.findByBookName(bookName);
            Order newOrder= new Order();
            newOrder.setBooks(Collections.singletonList(book));
            newOrder.setUser(user);
            orderRepository.save(newOrder);
            return new ResponseEntity<>("Order has been created",HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Book is not found",HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> deleteAuthOrderById(Long orderId) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        User user=userService.findByUsername(username);
        Order deletedOrder=orderRepository.findById(orderId).orElseThrow();
        if (authentication.isAuthenticated()&&deletedOrder.getUser().getId()== user.getId()){
            orderRepository.deleteById(orderId);
            return new ResponseEntity<>("Order has been deleted",HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("Order is not found or this order is not yours!!",HttpStatus.BAD_REQUEST);
    }
}
