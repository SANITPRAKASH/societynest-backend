package com.societynest.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsResponse {
    private Long totalResidents;
    private Long totalFlats;
    private Long totalComplaints;
    private Long openComplaints;
    private Long inProgressComplaints;
    private Long resolvedComplaints;
    private Long totalNotices;
    private Map<String, Long> complaintsByCategory;
    private Map<String, Long> complaintsByStatus;
}