package com.pooling.ledger.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rides")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private RideGroup rideGroup;

    @ManyToOne
    @JoinColumn(name = "paid_by_id")
    private Resident paidBy;

    private LocalDate date;

    private Double totalFare;

    private Double perPersonShare;

    private Boolean settled;

    @ManyToMany
    @JoinTable(
            name = "ride_participants",
            joinColumns = @JoinColumn(name = "ride_id"),
            inverseJoinColumns = @JoinColumn(name = "resident_id")
    )
    private List<Resident> participants = new ArrayList<>();

    public Ride() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RideGroup getRideGroup() {
        return rideGroup;
    }

    public void setRideGroup(RideGroup rideGroup) {
        this.rideGroup = rideGroup;
    }

    public Resident getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(Resident paidBy) {
        this.paidBy = paidBy;
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

    public List<Resident> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Resident> participants) {
        this.participants = participants;
    }
}