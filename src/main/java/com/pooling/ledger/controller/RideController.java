package com.pooling.ledger.controller;

import com.pooling.ledger.dto.ApiResponse;
import com.pooling.ledger.dto.RideRequestDTO;
import com.pooling.ledger.dto.RideResponseDTO;
import com.pooling.ledger.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
@CrossOrigin(origins = "http://localhost:5173")
public class RideController {

    @Autowired
    private RideService rideService;

    @PostMapping
    public ResponseEntity<ApiResponse<RideResponseDTO>> logRide(@RequestBody RideRequestDTO dto) {
        RideResponseDTO ride = rideService.logRide(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Ride logged successfully", ride));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RideResponseDTO>>> getRidesByGroup(@RequestParam Long groupId) {
        List<RideResponseDTO> rides = rideService.getRidesByGroup(groupId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Rides fetched successfully", rides));
    }

    @PatchMapping("/settle")
    public ResponseEntity<ApiResponse<String>> settleGroup(@RequestParam Long groupId) {
        String message = rideService.settleGroup(groupId);
        return ResponseEntity.ok(new ApiResponse<>(true, message, null));
    }
}