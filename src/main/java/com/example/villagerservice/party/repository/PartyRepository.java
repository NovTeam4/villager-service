package com.example.villagerservice.party.repository;

import com.example.villagerservice.party.domain.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {
}
