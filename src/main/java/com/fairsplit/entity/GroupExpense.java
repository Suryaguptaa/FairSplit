package com.fairsplit.entity;

import com.fairsplit.entity.auth.AppUser;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "group_expenses")
public class GroupExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    private Double amount;
    
    private String category;
    
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private AppGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paid_by_id", nullable = false)
    private AppUser paidBy;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "expense_splits",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<AppUser> splitBetween = new HashSet<>();

    public GroupExpense() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public AppGroup getGroup() { return group; }
    public void setGroup(AppGroup group) { this.group = group; }

    public AppUser getPaidBy() { return paidBy; }
    public void setPaidBy(AppUser paidBy) { this.paidBy = paidBy; }

    public Set<AppUser> getSplitBetween() { return splitBetween; }
    public void setSplitBetween(Set<AppUser> splitBetween) { this.splitBetween = splitBetween; }
}
