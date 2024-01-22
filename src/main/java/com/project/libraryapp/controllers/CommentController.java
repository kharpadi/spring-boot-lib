package com.project.libraryapp.controllers;

import com.project.libraryapp.entities.Comment;
import com.project.libraryapp.requests.AuthUserCreateComment;
import com.project.libraryapp.requests.CommentCreateRequest;
import com.project.libraryapp.requests.CommentUpdateRequest;
import com.project.libraryapp.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private CommentService commentService;
    @Autowired
    public CommentController(CommentService commentService){
        this.commentService=commentService;
    }

    @GetMapping
    public List<Comment>getAllComments(@RequestParam Optional<Long> userId, @RequestParam Optional<Long> bookId){
        return commentService.getAllComments(userId,bookId);
    }

    @GetMapping("/{commentId}")
    public Comment getOneComment(@PathVariable Long commentId){
        return commentService.getOneCommentById(commentId);
    }
//
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public Comment createComment(@RequestBody CommentCreateRequest comment){
        return commentService.createComment(comment);
    }

    @PostMapping
    public ResponseEntity<String> createAuthComment(@RequestBody AuthUserCreateComment comment){
        return commentService.createAuthComment(comment);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{commentId}")
    public Comment updateComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest comment){
        return commentService.updateCommentById(commentId,comment);
    }

    @PutMapping("/{commentId}")
    public Comment updateAuthComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest comment){
        return commentService.updateAuthCommentById(commentId,comment);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{commentId}")
    public void deleteComment(@PathVariable Long commentId){
        commentService.deleteCommentById(commentId);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteAuthComment(@PathVariable Long commentId){
        return commentService.deleteAuthCommentById(commentId);
    }
}
