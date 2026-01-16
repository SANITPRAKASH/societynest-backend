package com.societynest.backend.service;

import com.societynest.backend.dto.NoticeRequest;
import com.societynest.backend.entity.Notice;
import com.societynest.backend.entity.User;
import com.societynest.backend.repository.NoticeRepository;
import com.societynest.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private UserRepository userRepository;

    public Notice createNotice(NoticeRequest request, String email) {
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notice notice = new Notice();
        notice.setTitle(request.getTitle());
        notice.setContent(request.getContent());
        notice.setCreatedBy(admin);

        return noticeRepository.save(notice);
    }

    public List<Notice> getAllNotices() {
        return noticeRepository.findAllByOrderByCreatedAtDesc();
    }

    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }
}