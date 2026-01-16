package com.societynest.backend.controller;

import com.societynest.backend.dto.ApiResponse;
import com.societynest.backend.dto.NoticeRequest;
import com.societynest.backend.entity.Notice;
import com.societynest.backend.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Notice> createNotice(@RequestBody NoticeRequest request, 
                                                Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(noticeService.createNotice(request, email));
    }

    @GetMapping
    public ResponseEntity<List<Notice>> getAllNotices() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.ok(new ApiResponse(true, "Notice deleted successfully"));
    }
}