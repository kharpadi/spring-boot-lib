package com.project.libraryapp.requests;

import lombok.Data;

@Data
public class AuthUserCreateComment {
    private Long bookId;
    private String text;
}
