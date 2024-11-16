package com.example.SmartCommunity.service;

import com.example.SmartCommunity.model.Resident;
import com.example.SmartCommunity.repository.ResidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResidentService {

    @Autowired
    private ResidentRepository residentRepository;

    public List<Resident> findAll() {
        return residentRepository.findAll();
    }

    public Resident findById(Integer id) {
        return residentRepository.findById(id).orElse(null);
    }

    public Resident save(Resident resident) {
        return residentRepository.save(resident);
    }

    public Resident update(Resident resident) {
        return residentRepository.save(resident);
    }

    public void deleteById(Integer id) {
        residentRepository.deleteById(id);
    }
}
