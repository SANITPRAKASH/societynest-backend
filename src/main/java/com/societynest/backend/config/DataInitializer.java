package com.societynest.backend.config;

import com.societynest.backend.entity.*;
import com.societynest.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FlatRepository flatRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        // Create roles
        Role adminRole = createRoleIfNotExists("ADMIN");
        Role residentRole = createRoleIfNotExists("RESIDENT");

        // Create demo flats
        List<Flat> flats = createDemoFlats();

        // Create demo users
        User admin = createDemoAdmin(adminRole);
        List<User> residents = createDemoResidents(residentRole, flats);

        // Create demo notices
        createDemoNotices(admin);

        // Create demo complaints
        createDemoComplaints(residents);

        System.out.println("✅ Demo data initialized successfully!");
    }

    private Role createRoleIfNotExists(String roleName) {
        return roleRepository.findByName(roleName).orElseGet(() -> {
            Role role = new Role();
            role.setName(roleName);
            return roleRepository.save(role);
        });
    }

    private User createDemoAdmin(Role adminRole) {
        if (userRepository.findByEmail("admin@demo.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@demo.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Admin Manager");
            admin.setPhoneNumber("9999999999");
            admin.setRole(adminRole);
            admin.setIsApproved(true);
            return userRepository.save(admin);
        }
        return userRepository.findByEmail("admin@demo.com").get();
    }

    private List<Flat> createDemoFlats() {
        if (flatRepository.count() > 0) {
            return flatRepository.findAll();
        }

        String[] blocks = {"A", "B", "C"};
        String[] types = {"1BHK", "2BHK", "3BHK"};
        
        for (String block : blocks) {
            for (int floor = 1; floor <= 5; floor++) {
                for (int flatNum = 1; flatNum <= 4; flatNum++) {
                    Flat flat = new Flat();
                    flat.setBlock(block);
                    flat.setFloor(floor + " Floor");
                    flat.setFlatNumber(block + "-" + floor + "0" + flatNum);
                    flat.setType(types[new Random().nextInt(types.length)]);
                    flatRepository.save(flat);
                }
            }
        }
        return flatRepository.findAll();
    }

    private List<User> createDemoResidents(Role residentRole, List<Flat> flats) {
        if (userRepository.findByEmail("resident@demo.com").isPresent()) {
            return userRepository.findByIsApproved(true);
        }

        String[] names = {
            "John Doe", "Jane Smith", "Rahul Kumar", "Priya Sharma", 
            "Mike Johnson", "Sarah Williams", "Amit Patel", "Sneha Reddy",
            "David Brown", "Emily Davis", "Rajesh Singh", "Anita Verma",
            "Chris Wilson", "Lisa Anderson", "Vijay Gupta", "Pooja Mehta"
        };

        Random random = new Random();
        
        for (int i = 0; i < Math.min(16, flats.size()); i++) {
            String email = i == 0 ? "resident@demo.com" : 
                          names[i].toLowerCase().replace(" ", ".") + "@gmail.com";
            
            if (userRepository.findByEmail(email).isEmpty()) {
                User resident = new User();
                resident.setEmail(email);
                resident.setPassword(passwordEncoder.encode("resident123"));
                resident.setFullName(names[i]);
                resident.setPhoneNumber("98765432" + String.format("%02d", i));
                resident.setRole(residentRole);
                resident.setFlat(flats.get(i));
                resident.setIsApproved(true);
                resident.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(90)));
                userRepository.save(resident);
            }
        }
        
        return userRepository.findByIsApproved(true);
    }

    private void createDemoNotices(User admin) {
        if (noticeRepository.count() > 0) return;

        String[][] noticeData = {
            {"Society Meeting - January 2025", "Annual general meeting scheduled for January 15th at 6 PM in the community hall. All residents are requested to attend. Agenda: Budget approval, maintenance updates, new security measures."},
            {"Water Supply Maintenance", "Water supply will be temporarily suspended on January 12th from 10 AM to 2 PM for pipeline maintenance in Block A and B. Please store water accordingly."},
            {"New Parking Rules", "New parking guidelines effective from January 10th: Visitor parking only in designated areas. Resident vehicles must display parking stickers. Unauthorized vehicles will be towed."},
            {"Gym Timing Update", "Society gym timings updated: 6 AM - 10 AM and 5 PM - 10 PM on weekdays. Weekend: 7 AM - 8 PM. Please carry your membership card."},
            {"Festival Celebration", "Republic Day celebration on January 26th at 8 AM. Flag hoisting ceremony followed by cultural programs. All residents invited!"},
            {"Garbage Collection Schedule", "New garbage collection timings: Morning 7-9 AM, Evening 6-8 PM. Please cooperate with cleaning staff. Segregate wet and dry waste."}
        };

        Random random = new Random();
        for (int i = 0; i < noticeData.length; i++) {
            Notice notice = new Notice();
            notice.setTitle(noticeData[i][0]);
            notice.setContent(noticeData[i][1]);
            notice.setCreatedBy(admin);
            notice.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(30)));
            noticeRepository.save(notice);
        }
    }

    private void createDemoComplaints(List<User> residents) {
        if (complaintRepository.count() > 0) return;

        String[][] complaintData = {
            {"Plumbing", "Water Leakage in Bathroom", "Severe water leakage from bathroom ceiling. Water dripping continuously causing damage."},
            {"Electrical", "Power Fluctuation Issue", "Frequent power fluctuations in flat causing damage to appliances. Needs immediate attention."},
            {"Cleaning", "Staircase Not Cleaned", "Staircase on 3rd floor has not been cleaned for a week. Dust accumulation."},
            {"Security", "Gate Lock Broken", "Main gate lock is not working properly. Security concern for all residents."},
            {"Plumbing", "Drainage Blockage", "Kitchen sink drainage blocked. Water not flowing properly."},
            {"Electrical", "Lift Not Working", "Lift in Block B stopped working since yesterday. Very inconvenient for senior citizens."},
            {"Maintenance", "Garden Maintenance Required", "Society garden needs trimming and watering. Plants are drying up."},
            {"Plumbing", "Water Pressure Low", "Very low water pressure on 5th floor. Difficult to use during peak hours."},
            {"Security", "CCTV Camera Not Working", "CCTV camera near parking area not functioning for 3 days."},
            {"Cleaning", "Garbage Not Collected", "Garbage bins overflowing. Not collected for 2 days. Bad smell."},
            {"Electrical", "Street Light Not Working", "Street light near Block C not working. Dark at night, safety issue."},
            {"Maintenance", "Swing Set Broken", "Children's swing in play area broken. Needs repair urgently."}
        };

        Complaint.ComplaintStatus[] statuses = {
            Complaint.ComplaintStatus.RESOLVED,
            Complaint.ComplaintStatus.IN_PROGRESS,
            Complaint.ComplaintStatus.OPEN
        };

        Random random = new Random();
        
        for (int i = 0; i < complaintData.length; i++) {
            Complaint complaint = new Complaint();
            complaint.setCategory(complaintData[i][0]);
            complaint.setTitle(complaintData[i][1]);
            complaint.setDescription(complaintData[i][2]);
            complaint.setRaisedBy(residents.get(random.nextInt(residents.size())));
            complaint.setStatus(statuses[random.nextInt(statuses.length)]);
            complaint.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(45)));
            
            if (complaint.getStatus() == Complaint.ComplaintStatus.RESOLVED) {
                complaint.setResolvedAt(complaint.getCreatedAt().plusDays(random.nextInt(7) + 1));
            }
            
            complaintRepository.save(complaint);
        }
    }
}