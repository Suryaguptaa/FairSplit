package com.fairsplit.entity;

import com.fairsplit.entity.auth.AppUser;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "settlements")
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private AppGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paid_by_id", nullable = false)
    private AppUser paidBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paid_to_id", nullable = false)
    private AppUser paidTo;

    public Settlement() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public AppGroup getGroup() { return group; }
    public void setGroup(AppGroup group) { this.group = group; }

    public AppUser getPaidBy() { return paidBy; }
    public void setPaidBy(AppUser paidBy) { this.paidBy = paidBy; }

    public AppUser getPaidTo() { return paidTo; }
    public void setPaidTo(AppUser paidTo) { this.paidTo = paidTo; }
}
