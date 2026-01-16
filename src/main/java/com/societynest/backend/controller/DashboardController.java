package com.societynest.backend.controller;

import com.societynest.backend.dto.DashboardStatsResponse;
import com.societynest.backend.dto.PublicStatsResponse;
import com.societynest.backend.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/public-stats")
    public ResponseEntity<PublicStatsResponse> getPublicStats() {
        return ResponseEntity.ok(dashboardService.getPublicStats());
    }

    @GetMapping("/admin-stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardStatsResponse> getAdminStats() {
        return ResponseEntity.ok(dashboardService.getAdminDashboardStats());
    }
}