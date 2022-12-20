package com.example.villagerservice.party.dto;

import com.example.villagerservice.party.domain.PartyChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartyChatMessageDto {
    private Long id;
    private Long roomId;
    private String writerNickname;
    private String message;

    public static PartyChatMessageDto toDto(PartyChatMessage partyChatMessage){
        return PartyChatMessageDto.builder()
            .id(partyChatMessage.getId())
            .message(partyChatMessage.getMessage())
            .roomId(partyChatMessage.getRoomId())
            .writerNickname(partyChatMessage.getWriterNickname())
            .build();
    }
}
