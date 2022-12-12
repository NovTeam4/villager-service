package com.example.villagerservice.party.repository;

import com.example.villagerservice.party.domain.Party;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long> {
    boolean existsById(Long partyId);
    Page<Party> findAll(Pageable pageable);
}
