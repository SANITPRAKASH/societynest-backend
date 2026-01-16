package com.societynest.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "flats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String flatNumber;
    
    @Column(nullable = false)
    private String block;
    
    @Column(nullable = false)
    private String floor;
    
    @Column(nullable = false)
    private String type; // 1BHK, 2BHK, 3BHK
}