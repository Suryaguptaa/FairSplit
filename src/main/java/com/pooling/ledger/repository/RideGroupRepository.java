package com.pooling.ledger.repository;

import com.pooling.ledger.entity.RideGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideGroupRepository extends JpaRepository<RideGroup, Long> {
}