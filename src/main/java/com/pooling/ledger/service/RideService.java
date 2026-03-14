package com.pooling.ledger.service;

import com.pooling.ledger.dto.RideRequestDTO;
import com.pooling.ledger.dto.RideResponseDTO;
import com.pooling.ledger.entity.Resident;
import com.pooling.ledger.entity.Ride;
import com.pooling.ledger.entity.RideGroup;
import com.pooling.ledger.repository.ResidentRepository;
import com.pooling.ledger.repository.RideGroupRepository;
import com.pooling.ledger.repository.RideRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private ResidentRepository residentRepository;

    @Autowired
    private RideGroupRepository rideGroupRepository;

    @Transactional
    public RideResponseDTO logRide(RideRequestDTO dto) {
        RideGroup rideGroup = rideGroupRepository.findById(dto.getRideGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + dto.getRideGroupId()));

        Resident paidBy = residentRepository.findById(dto.getPaidById())
                .orElseThrow(() -> new RuntimeException("Resident not found with id: " + dto.getPaidById()));

        List<Resident> participants = new ArrayList<>();
        for (Long participantId : dto.getParticipantIds()) {
            Resident participant = residentRepository.findById(participantId)
                    .orElseThrow(() -> new RuntimeException("Resident not found with id: " + participantId));
            participants.add(participant);
        }

        boolean payerIsParticipant = participants.stream()
                .anyMatch(p -> p.getId().equals(paidBy.getId()));

        if (!payerIsParticipant) {
            throw new RuntimeException("Payer must be one of the participants");
        }

        double perPersonShare = dto.getTotalFare() / participants.size();

        for (Resident participant : participants) {
            participant.setBalance(participant.getBalance() - perPersonShare);
            residentRepository.save(participant);
        }

        paidBy.setBalance(paidBy.getBalance() + dto.getTotalFare());
        residentRepository.save(paidBy);

        Ride ride = new Ride();
        ride.setRideGroup(rideGroup);
        ride.setPaidBy(paidBy);
        ride.setDate(dto.getDate());
        ride.setTotalFare(dto.getTotalFare());
        ride.setPerPersonShare(perPersonShare);
        ride.setParticipants(participants);
        ride.setSettled(false);

        Ride savedRide = rideRepository.save(ride);

        return mapToResponseDTO(savedRide);
    }

    public List<RideResponseDTO> getRidesByGroup(Long groupId) {
        return rideRepository.findByRideGroupId(groupId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public String settleGroup(Long groupId) {
        List<Ride> unsettledRides = rideRepository.findByRideGroupIdAndSettled(groupId, false);

        for (Ride ride : unsettledRides) {
            ride.setSettled(true);
            rideRepository.save(ride);
        }

        RideGroup group = rideGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id: " + groupId));

        for (Resident member : group.getMembers()) {
            member.setBalance(0.0);
            residentRepository.save(member);
        }

        return "Group " + group.getName() + " has been settled. All balances reset to 0.";
    }

    private RideResponseDTO mapToResponseDTO(Ride ride) {
        List<String> participantNames = ride.getParticipants()
                .stream()
                .map(Resident::getName)
                .collect(Collectors.toList());

        return new RideResponseDTO(
                ride.getId(),
                ride.getRideGroup().getName(),
                ride.getPaidBy().getName(),
                ride.getDate(),
                ride.getTotalFare(),
                ride.getPerPersonShare(),
                ride.getSettled(),
                participantNames
        );
    }
}