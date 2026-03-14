package com.pooling.ledger.service;

import com.pooling.ledger.dto.ResidentDTO;
import com.pooling.ledger.entity.Resident;
import com.pooling.ledger.repository.ResidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResidentService {

    @Autowired
    private ResidentRepository residentRepository;

    public ResidentDTO createResident(ResidentDTO dto) {
        Resident resident = new Resident();
        resident.setName(dto.getName());
        resident.setEmail(dto.getEmail());
        resident.setFlatNumber(dto.getFlatNumber());
        resident.setBalance(0.0);
        Resident saved = residentRepository.save(resident);
        return mapToDTO(saved);
    }

    public List<ResidentDTO> getAllResidents() {
        return residentRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ResidentDTO getResidentById(Long id) {
        Resident resident = residentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resident not found with id: " + id));
        return mapToDTO(resident);
    }

    public Double getBalance(Long id) {
        Resident resident = residentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resident not found with id: " + id));
        return resident.getBalance();
    }

    private ResidentDTO mapToDTO(Resident resident) {
        return new ResidentDTO(
                resident.getId(),
                resident.getName(),
                resident.getEmail(),
                resident.getFlatNumber(),
                resident.getBalance()
        );
    }
}