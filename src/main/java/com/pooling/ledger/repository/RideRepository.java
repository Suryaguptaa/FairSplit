package com.pooling.ledger.repository;

import com.pooling.ledger.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findByRideGroupId(Long groupId);
    List<Ride> findByRideGroupIdAndSettled(Long groupId, Boolean settled);
}