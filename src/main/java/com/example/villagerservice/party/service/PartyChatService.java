package com.example.villagerservice.party.service;

import static com.example.villagerservice.party.exception.PartyErrorCode.PARTY_CHAT_ROOM_NOT_FOUND;

import com.example.villagerservice.party.domain.PartyChatMessage;
import com.example.villagerservice.party.domain.PartyChatRoom;
import com.example.villagerservice.party.dto.PartyChatMessageDto;
import com.example.villagerservice.party.exception.PartyException;
import com.example.villagerservice.party.repository.PartyChatMessageRepository;
import com.example.villagerservice.party.repository.PartyChatRoomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyChatService {
    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달
    private final PartyChatRoomRepository partyChatRoomRepository;
    private final PartyChatMessageRepository partyChatMessageRepository;

    /**
     * 처음 접속 시
     * @param message
     */
    public void enter(PartyChatMessageDto message) {
        message.setMessage(message.getWriterNickname() + "님이 모임에 참여하였습니다.");

        // 새로 참여했으니 원래 있었던 대화내용 가져오기
        // 채팅방
        PartyChatRoom partyChatRoom = partyChatRoomRepository.findById(message.getRoomId()).orElseThrow(
            () -> new PartyException(PARTY_CHAT_ROOM_NOT_FOUND)
        );
        // 채팅방 메세지 가져와서 메세지 세팅
        List<PartyChatMessage> partyChatMessageList = partyChatRoom.getPartyChatMessageList();
        for(PartyChatMessage partyChatMessage: partyChatMessageList){
            message.setWriterNickname(partyChatMessage.getWriterNickname());
            message.setMessage(partyChatMessage.getMessage());
        }

        // 가공한 메세지 전송
        template.convertAndSend("/sub/chat/ room/" + message.getRoomId(), message);
    }

    /**
     * 메세지 전송
     * @param message
     */
    public void message(PartyChatMessageDto message){
        // 바로 메세지 전송
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);

        // DB에 채팅내용 저장
        PartyChatRoom partyChatRoom = partyChatRoomRepository.findById(message.getId()).orElseThrow(
            () -> new PartyException(PARTY_CHAT_ROOM_NOT_FOUND)
        );
        PartyChatMessage partyChatMessage = PartyChatMessage.toEntity(message, partyChatRoom);
        // 메세지 저장
        partyChatMessageRepository.save(partyChatMessage);
        // 채팅방 업데이트
        partyChatRoom.update(partyChatMessage);
        // 채팅방 저장
        partyChatRoomRepository.save(partyChatRoom);
    }

    /**
     * 채팅방 개설
     * @param hostNickname
     * @param roomName
     */
    public void create(String hostNickname, String roomName) {
        partyChatRoomRepository.save(PartyChatRoom.toEntity(hostNickname, roomName));
    }
}
