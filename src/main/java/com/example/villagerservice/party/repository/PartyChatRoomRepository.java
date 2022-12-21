package com.example.villagerservice.party.repository;

import com.example.villagerservice.party.domain.PartyChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyChatRoomRepository extends JpaRepository<PartyChatRoom, Long> {
}
