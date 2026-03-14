package com.pooling.ledger.service;

import com.pooling.ledger.dto.ResidentDTO;
import com.pooling.ledger.dto.RideGroupDTO;
import com.pooling.ledger.entity.Resident;
import com.pooling.ledger.entity.RideGroup;
import com.pooling.ledger.repository.ResidentRepository;
import com.pooling.ledger.repository.RideGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RideGroupService {

    @Autowired
    private RideGroupRepository rideGroupRepository;

    @Autowired
    private ResidentRepository residentRepository;

    public RideGroupDTO createGroup(RideGroupDTO dto) {
        RideGroup group = new RideGroup();
        group.setName(dto.getName());
        group.setDescription(dto.getDescription());
        RideGroup saved = rideGroupRepository.save(group);
        return mapToDTO(saved);
    }

    public List<RideGroupDTO> getAllGroups() {
        return rideGroupRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public RideGroupDTO getGroupById(Long id) {
        RideGroup group = rideGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + id));
        return mapToDTO(group);
    }

    public String addMemberToGroup(Long groupId, Long residentId) {
        RideGroup group = rideGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new RuntimeException("Resident not found with id: " + residentId));
        group.getMembers().add(resident);
        rideGroupRepository.save(group);
        return "Resident " + resident.getName() + " added to group " + group.getName();
    }

    public List<ResidentDTO> getMembersWithBalances(Long groupId) {
        RideGroup group = rideGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));
        return group.getMembers()
                .stream()
                .map(r -> new ResidentDTO(r.getId(), r.getName(), r.getEmail(), r.getFlatNumber(), r.getBalance()))
                .collect(Collectors.toList());
    }

    private RideGroupDTO mapToDTO(RideGroup group) {
        return new RideGroupDTO(group.getId(), group.getName(), group.getDescription());
    }
}