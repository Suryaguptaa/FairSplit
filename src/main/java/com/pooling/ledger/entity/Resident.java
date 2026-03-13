package com.pooling.ledger.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "residents")
public class Resident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String flatNumber;

    private Double balance;

    @ManyToMany(mappedBy = "members")
    private List<RideGroup> rideGroups = new ArrayList<>();

    public Resident() {}

    public Resident(String name, String email, String flatNumber, Double balance) {
        this.name = name;
        this.email = email;
        this.flatNumber = flatNumber;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public List<RideGroup> getRideGroups() {
        return rideGroups;
    }

    public void setRideGroups(List<RideGroup> rideGroups) {
        this.rideGroups = rideGroups;
    }
}