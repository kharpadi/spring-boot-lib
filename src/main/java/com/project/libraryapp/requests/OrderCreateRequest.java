package com.project.libraryapp.requests;

import lombok.Data;

@Data
public class OrderCreateRequest {
    private long Id;
    private long userId;
    private long bookId;
}
