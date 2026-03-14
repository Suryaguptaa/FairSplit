package com.pooling.ledger.controller;

import com.pooling.ledger.dto.ApiResponse;
import com.pooling.ledger.dto.ResidentDTO;
import com.pooling.ledger.service.ResidentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/residents")
@CrossOrigin(origins = "http://localhost:5173")
public class ResidentController {

    @Autowired
    private ResidentService residentService;

    @PostMapping
    public ResponseEntity<ApiResponse<ResidentDTO>> createResident(@RequestBody ResidentDTO dto) {
        ResidentDTO created = residentService.createResident(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Resident registered successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ResidentDTO>>> getAllResidents() {
        List<ResidentDTO> residents = residentService.getAllResidents();
        return ResponseEntity.ok(new ApiResponse<>(true, "Residents fetched successfully", residents));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResidentDTO>> getResidentById(@PathVariable Long id) {
        ResidentDTO resident = residentService.getResidentById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Resident fetched successfully", resident));
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<ApiResponse<Double>> getBalance(@PathVariable Long id) {
        Double balance = residentService.getBalance(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Balance fetched successfully", balance));
    }
}