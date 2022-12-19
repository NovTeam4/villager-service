package com.example.villagerservice.party.repository;

import com.example.villagerservice.party.domain.PartyChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyChatMessageRepository extends JpaRepository<PartyChatMessage, Long> {
}
