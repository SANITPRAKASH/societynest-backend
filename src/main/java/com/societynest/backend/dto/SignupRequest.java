package com.societynest.backend.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
    private String fullName;
    private String phoneNumber;
    private Long flatId;
}