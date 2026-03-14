package com.pooling.ledger.dto;

import java.time.LocalDate;
import java.util.List;

public class RideResponseDTO {

    private Long id;
    private String rideGroupName;
    private String paidByName;
    private LocalDate date;
    private Double totalFare;
    private Double perPersonShare;
    private Boolean settled;
    private List<String> participantNames;

    public RideResponseDTO() {}

    public RideResponseDTO(Long id, String rideGroupName, String paidByName, LocalDate date,
                           Double totalFare, Double perPersonShare, Boolean settled, List<String> participantNames) {
        this.id = id;
        this.rideGroupName = rideGroupName;
        this.paidByName = paidByName;
        this.date = date;
        this.totalFare = totalFare;
        this.perPersonShare = perPersonShare;
        this.settled = settled;
        this.participantNames = participantNames;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRideGroupName() {
        return rideGroupName;
    }

    public void setRideGroupName(String rideGroupName) {
        this.rideGroupName = rideGroupName;
    }

    public String getPaidByName() {
        return paidByName;
    }

    public void setPaidByName(String paidByName) {
        this.paidByName = paidByName;
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

    public Double getPerPersonShare() {
        return perPersonShare;
    }

    public void setPerPersonShare(Double perPersonShare) {
        this.perPersonShare = perPersonShare;
    }

    public Boolean getSettled() {
        return settled;
    }

    public void setSettled(Boolean settled) {
        this.settled = settled;
    }

    public List<String> getParticipantNames() {
        return participantNames;
    }

    public void setParticipantNames(List<String> participantNames) {
        this.participantNames = participantNames;
    }
}