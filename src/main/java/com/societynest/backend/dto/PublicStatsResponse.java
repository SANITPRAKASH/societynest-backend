package com.societynest.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PublicStatsResponse {
    private Long totalApartments;
    private Long activeResidents;
    private Long complaintsResolved;
    private Long noticesPublished;
}