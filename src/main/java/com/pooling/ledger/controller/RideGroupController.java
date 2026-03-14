package com.pooling.ledger.controller;

import com.pooling.ledger.dto.ApiResponse;
import com.pooling.ledger.dto.ResidentDTO;
import com.pooling.ledger.dto.RideGroupDTO;
import com.pooling.ledger.service.RideGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "https://vehicle-pooling-ledger-frontend.vercel.app")
public class RideGroupController {

    @Autowired
    private RideGroupService rideGroupService;

    @PostMapping
    public ResponseEntity<ApiResponse<RideGroupDTO>> createGroup(@RequestBody RideGroupDTO dto) {
        RideGroupDTO created = rideGroupService.createGroup(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Group created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RideGroupDTO>>> getAllGroups() {
        List<RideGroupDTO> groups = rideGroupService.getAllGroups();
        return ResponseEntity.ok(new ApiResponse<>(true, "Groups fetched successfully", groups));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RideGroupDTO>> getGroupById(@PathVariable Long id) {
        RideGroupDTO group = rideGroupService.getGroupById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Group fetched successfully", group));
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<ApiResponse<String>> addMember(@PathVariable Long id, @RequestParam Long residentId) {
        String message = rideGroupService.addMemberToGroup(id, residentId);
        return ResponseEntity.ok(new ApiResponse<>(true, message, null));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<ApiResponse<List<ResidentDTO>>> getMembers(@PathVariable Long id) {
        List<ResidentDTO> members = rideGroupService.getMembersWithBalances(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Members fetched successfully", members));
    }
}