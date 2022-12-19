package com.example.villagerservice.party.repository;

import com.example.villagerservice.party.domain.PartyChatMessage;
import com.example.villagerservice.party.domain.PartyChatRoom;
import com.example.villagerservice.party.dto.PartyChatMessageDto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyChatMessageRepository extends JpaRepository<PartyChatMessage, Long> {
    List<PartyChatMessageDto> findAllByPartyRoomId(Long roomId);
}
