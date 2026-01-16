package com.societynest.backend.service;

import com.societynest.backend.dto.DashboardStatsResponse;
import com.societynest.backend.dto.PublicStatsResponse;
import com.societynest.backend.entity.Complaint;
import com.societynest.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FlatRepository flatRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    public PublicStatsResponse getPublicStats() {
        Long totalFlats = flatRepository.count();
        Long approvedResidents = userRepository.findByIsApproved(true).stream()
                .filter(user -> user.getRole().getName().equals("RESIDENT"))
                .count();
        Long resolvedComplaints = complaintRepository.findAll().stream()
                .filter(c -> c.getStatus() == Complaint.ComplaintStatus.RESOLVED)
                .count();
        Long totalNotices = noticeRepository.count();

        return new PublicStatsResponse(totalFlats, approvedResidents, resolvedComplaints, totalNotices);
    }

    public DashboardStatsResponse getAdminDashboardStats() {
        List<Complaint> allComplaints = complaintRepository.findAll();
        
        Long totalResidents = userRepository.findByIsApproved(true).stream()
                .filter(user -> user.getRole().getName().equals("RESIDENT"))
                .count();
        
        Long openComplaints = allComplaints.stream()
                .filter(c -> c.getStatus() == Complaint.ComplaintStatus.OPEN)
                .count();
        
        Long inProgressComplaints = allComplaints.stream()
                .filter(c -> c.getStatus() == Complaint.ComplaintStatus.IN_PROGRESS)
                .count();
        
        Long resolvedComplaints = allComplaints.stream()
                .filter(c -> c.getStatus() == Complaint.ComplaintStatus.RESOLVED)
                .count();

        // Complaints by category
        Map<String, Long> byCategory = allComplaints.stream()
                .collect(Collectors.groupingBy(Complaint::getCategory, Collectors.counting()));

        // Complaints by status
        Map<String, Long> byStatus = new HashMap<>();
        byStatus.put("OPEN", openComplaints);
        byStatus.put("IN_PROGRESS", inProgressComplaints);
        byStatus.put("RESOLVED", resolvedComplaints);

        DashboardStatsResponse stats = new DashboardStatsResponse();
        stats.setTotalResidents(totalResidents);
        stats.setTotalFlats(flatRepository.count());
        stats.setTotalComplaints((long) allComplaints.size());
        stats.setOpenComplaints(openComplaints);
        stats.setInProgressComplaints(inProgressComplaints);
        stats.setResolvedComplaints(resolvedComplaints);
        stats.setTotalNotices(noticeRepository.count());
        stats.setComplaintsByCategory(byCategory);
        stats.setComplaintsByStatus(byStatus);

        return stats;
    }
}