package com.pooling.ledger.repository;

import com.pooling.ledger.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, Long> {
    Optional<Resident> findByEmail(String email);
}