package com.project.libraryapp.requests;

import lombok.Data;

@Data
public class AuthResponseDto {
    private String accessToken;
    private String tokeType="Bearer ";

    public AuthResponseDto(String accessToken){
        this.accessToken=accessToken;
    }
}
