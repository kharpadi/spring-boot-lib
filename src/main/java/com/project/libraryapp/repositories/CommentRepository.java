package com.project.libraryapp.repositories;

import com.project.libraryapp.entities.Book;
import com.project.libraryapp.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByUserIdAndBookId(Long userId, Long bookId);

    List<Comment> findByUserId(Long userId);

    List<Comment> findByBookId(Long bookId);
}
