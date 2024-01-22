package com.project.libraryapp.repositories;

import com.project.libraryapp.entities.Book;
import com.project.libraryapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    Optional<Book>findByBookName(String bookName);
    List<Book> findByBookNameContainingIgnoreCase(String bookName);
}
