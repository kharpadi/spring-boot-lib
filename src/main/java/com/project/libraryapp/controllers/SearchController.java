package com.project.libraryapp.controllers;

import com.project.libraryapp.entities.Book;
import com.project.libraryapp.services.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    private BookService bookService;
    public SearchController(BookService bookService){
        this.bookService=bookService;
    }

    @GetMapping
    public ResponseEntity<?> searchBook(@RequestParam String bookName){
        return bookService.findByAdContainingIgnoreCase(bookName);
    }
}
