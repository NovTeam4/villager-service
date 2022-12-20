package com.example.villagerservice.party.domain;

import com.example.villagerservice.common.domain.BaseEntity;
import com.example.villagerservice.party.dto.PartyChatMessageDto;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartyChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "party_chat_room_id")
    private PartyChatRoom partyChatRoom;
    private Long roomId;

    private String writerNickname;

    private String message;

    public static PartyChatMessage toEntity(PartyChatMessageDto message, PartyChatRoom room) {
        return PartyChatMessage.builder()
            .partyChatRoom(room)
            .roomId(room.getId())
            .writerNickname(message.getWriterNickname())
            .message(message.getMessage())
            .build();
    }
}