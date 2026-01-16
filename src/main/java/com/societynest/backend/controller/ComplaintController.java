package com.societynest.backend.controller;

import com.societynest.backend.dto.ApiResponse;
import com.societynest.backend.dto.ComplaintRequest;
import com.societynest.backend.entity.Complaint;
import com.societynest.backend.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @PostMapping
    public ResponseEntity<Complaint> createComplaint(@RequestBody ComplaintRequest request,
                                                      Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(complaintService.createComplaint(request, email));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Complaint>> getAllComplaints() {
        return ResponseEntity.ok(complaintService.getAllComplaints());
    }

    @GetMapping("/my")
    public ResponseEntity<List<Complaint>> getMyComplaints(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(complaintService.getMyComplaints(email));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Complaint> updateComplaintStatus(@PathVariable Long id,
                                                            @RequestParam String status) {
        Complaint.ComplaintStatus complaintStatus = Complaint.ComplaintStatus.valueOf(status);
        return ResponseEntity.ok(complaintService.updateComplaintStatus(id, complaintStatus));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
        return ResponseEntity.ok(new ApiResponse(true, "Complaint deleted successfully"));
    }
}