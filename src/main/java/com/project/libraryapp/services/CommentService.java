package com.project.libraryapp.services;

import com.project.libraryapp.entities.Book;
import com.project.libraryapp.entities.Comment;
import com.project.libraryapp.entities.User;
import com.project.libraryapp.repositories.CommentRepository;
import com.project.libraryapp.requests.AuthUserCreateComment;
import com.project.libraryapp.requests.CommentCreateRequest;
import com.project.libraryapp.requests.CommentUpdateRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
@Service
public class CommentService {

    private AuthenticationManager authenticationManager;
    private CommentRepository commentRepository;
    private UserService userService;
    private BookService bookService;
    private OrderService orderService;
    @Autowired
    public CommentService(OrderService orderService,AuthenticationManager authenticationManager,CommentRepository commentRepository,UserService userService, BookService bookService){
        this.orderService=orderService;
        this.authenticationManager=authenticationManager;
        this.commentRepository=commentRepository;
        this.userService=userService;
        this.bookService=bookService;
    }
    public List<Comment> getAllComments(Optional<Long>userId, Optional<Long> bookId) {
        if (userId.isPresent()&&bookId.isPresent()){
            return commentRepository.findByUserIdAndBookId(userId.get(),bookId.get());
        } else if (userId.isPresent()) {
            return commentRepository.findByUserId(userId.get());
        } else if (bookId.isPresent()) {
            return commentRepository.findByBookId(bookId.get());
        }else {
            return commentRepository.findAll();
        }
    }

    public Comment getOneCommentById(long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }
//
    public Comment createComment(CommentCreateRequest comment) {
        User user=userService.getOneUser(comment.getUserId());
        Book book=bookService.getOneBook(comment.getBookId());
        if (user!=null&&book!=null){
            Comment toSaveComment= new Comment();
            toSaveComment.setId(comment.getId());
            toSaveComment.setUser(user);
            toSaveComment.setBook(book);
            toSaveComment.setText(comment.getText());
            return commentRepository.save(toSaveComment);
        }else {
            return null;
        }
    }
    public ResponseEntity<String> createAuthComment(AuthUserCreateComment comment) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()){
            Book book=bookService.getOneBook(comment.getBookId());
            String username=authentication.getName();
            User user=userService.findByUsername(username);
            Comment newComment=new Comment();
            newComment.setText(comment.getText());
            newComment.setBook(book);
            newComment.setUser(user);
            commentRepository.save(newComment);
            return new ResponseEntity<>("Comment has been committed", HttpStatus.OK);
        }
        return null;
    }

    public Comment updateCommentById(long commentId, CommentUpdateRequest newComment) {
       Optional<Comment> comment= commentRepository.findById(commentId);
        if (comment.isPresent()){
            Comment updatedComment=comment.get();
            updatedComment.setText(newComment.getText());
            return commentRepository.save(updatedComment);
        }else {
            return null;
        }
    }

    public void deleteCommentById(long commentId) {
        commentRepository.deleteById(commentId);
    }

    public Comment updateAuthCommentById(Long commentId, CommentUpdateRequest comment) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        User user=userService.findByUsername(username);
        Comment updatedComment= (Comment) commentRepository.findByUserIdAndBookId(user.getId(),commentId);
        if (authentication.isAuthenticated()&&updatedComment!=null){
            updatedComment.setUser(user);
            updatedComment.setText(comment.getText());
            commentRepository.save(updatedComment);
            return updatedComment;
        }
        else
            return null;
    }

    public ResponseEntity<String> deleteAuthCommentById(Long commentId) {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        User user=userService.findByUsername(username);
        Comment deletedComment=(Comment)commentRepository.findByUserIdAndBookId(user.getId(),commentId);
        if (authentication.isAuthenticated()&&deletedComment!=null){
            commentRepository.deleteById(commentId);
            return new ResponseEntity<>("Comment has been deleted",HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("There is no comment or this comment is not yours!!",HttpStatus.BAD_REQUEST);



    }
}
