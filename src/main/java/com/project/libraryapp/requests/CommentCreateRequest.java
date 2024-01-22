package com.project.libraryapp.requests;

import lombok.Data;

@Data
public class CommentCreateRequest {
    private long Id;
    private long userId;
    private long bookId;
    private String text;
}
