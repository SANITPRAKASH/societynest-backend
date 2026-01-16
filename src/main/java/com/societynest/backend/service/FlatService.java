package com.societynest.backend.service;

import com.societynest.backend.entity.Flat;
import com.societynest.backend.repository.FlatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlatService {

    @Autowired
    private FlatRepository flatRepository;

    public Flat createFlat(Flat flat) {
        return flatRepository.save(flat);
    }

    public List<Flat> getAllFlats() {
        return flatRepository.findAll();
    }

    public Flat getFlatById(Long id) {
        return flatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flat not found"));
    }

    public void deleteFlat(Long id) {
        flatRepository.deleteById(id);
    }
}