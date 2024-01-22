package com.project.libraryapp.services;

import com.project.libraryapp.entities.Book;
import com.project.libraryapp.entities.User;
import com.project.libraryapp.repositories.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private BookRepository bookRepository;
    public BookService(BookRepository bookRepository){
        this.bookRepository=bookRepository;
    }
    public Book findByBookName(String bookName){
        Book book=bookRepository.findByBookName(bookName).orElseThrow();
        return book;
    }

    public List<Book>retrieveAllBooks(){
        return bookRepository.findAll();
    }

    public Book createBook(Book book){
        return bookRepository.save(book);
    }

    public Book getOneBook(Long bookId){
        return bookRepository.findById(bookId).orElse(null);
    }

    public Book updateOneBook(long bookId,Book newBook) {
        Optional<Book> book=bookRepository.findById(bookId);
        if (book.isPresent()){
            Book foundBook=book.get();
            foundBook.setBookName(newBook.getBookName());
            bookRepository.save(foundBook);
            return foundBook;
        }else {
            return null;
        }
    }
    public void deleteBook(long bookId) {
        bookRepository.deleteById(bookId);
    }

    public ResponseEntity<?> findByAdContainingIgnoreCase(String bookName) {
        List<Book>searchedBooks= bookRepository.findByBookNameContainingIgnoreCase(bookName);
        if (!searchedBooks.isEmpty()){
            return ResponseEntity.ok(searchedBooks);
        }
        else {
            return new ResponseEntity<>("Book is not found", HttpStatus.NOT_FOUND);
        }
    }
}
