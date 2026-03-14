package com.pooling.ledger.dto;

import java.time.LocalDate;
import java.util.List;

public class RideRequestDTO {

    private Long rideGroupId;
    private Long paidById;
    private LocalDate date;
    private Double totalFare;
    private List<Long> participantIds;

    public RideRequestDTO() {}

    public RideRequestDTO(Long rideGroupId, Long paidById, LocalDate date, Double totalFare, List<Long> participantIds) {
        this.rideGroupId = rideGroupId;
        this.paidById = paidById;
        this.date = date;
        this.totalFare = totalFare;
        this.participantIds = participantIds;
    }

    public Long getRideGroupId() {
        return rideGroupId;
    }

    public void setRideGroupId(Long rideGroupId) {
        this.rideGroupId = rideGroupId;
    }

    public Long getPaidById() {
        return paidById;
    }

    public void setPaidById(Long paidById) {
        this.paidById = paidById;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(Double totalFare) {
        this.totalFare = totalFare;
    }

    public List<Long> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<Long> participantIds) {
        this.participantIds = participantIds;
    }
}