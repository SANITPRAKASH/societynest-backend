package com.societynest.backend.repository;

import com.societynest.backend.entity.Complaint;
import com.societynest.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByRaisedBy(User user);
    List<Complaint> findAllByOrderByCreatedAtDesc();
}