package com.project.libraryapp.controllers;

import com.project.libraryapp.entities.Book;
import com.project.libraryapp.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {
    private BookService bookService;
    public BookController(BookService bookService){
        this.bookService=bookService;
    }
    @GetMapping
    public List<Book>retrieveAllBooks(){
        return bookService.retrieveAllBooks();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public Book createBook(@RequestBody Book newBook){
        return bookService.createBook(newBook);
    }

    @GetMapping("/{bookId}")
    public Book getOneBook(@PathVariable long bookId){
        return bookService.getOneBook(bookId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{bookId}")
    public Book updateOneBook(@PathVariable long bookId,@RequestBody Book book){
        return bookService.updateOneBook(bookId,book);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{bookId}")
    public void deleteBook(@PathVariable long bookId){
        bookService.deleteBook(bookId);
    }
}
