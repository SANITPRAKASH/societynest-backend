package com.societynest.backend.dto;

import lombok.Data;

@Data
public class ComplaintRequest {
    private String category;
    private String title;
    private String description;
}