package com.example.villagerservice.party.api;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.party.dto.PartyChatMessageDto;
import com.example.villagerservice.party.service.PartyChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/parties")
@MessageMapping("/api/v1/parties")
public class PartyChatController {
    private final PartyChatService partyChatService;

    //Client 가 SEND 할 수 있는 경로
    //stompConfig 에서 설정한 applicationDestinationPrefixes 와 @MessageMapping 경로가 병합됨
    //"/pub/chat/enter" 새로 들어왔으니 가공하여(pub) 메세지 전송
    @MessageMapping(value = "/chat/enter")
    public void enter(PartyChatMessageDto message) {
        partyChatService.enter(message);
    }

    @MessageMapping(value = "/chat/message")
    public void message(PartyChatMessageDto message) {
        partyChatService.message(message);
    }

    //채팅방 개설
    @PostMapping(value = "/room/{roomName}")
    public void create(@AuthenticationPrincipal Member member, @PathVariable String roomName){
        partyChatService.create(member.getMemberDetail().getNickname(), roomName);
    }
//    @MessageMapping 을 통해 WebSocket 으로 들어오는 메세지 발행을 처리한다.
//    Client 에서는 prefix 를 붙여 "/pub/chat/enter"로 발행 요청을 하면
//    Controller 가 해당 메세지를 받아 처리하는데,
//    메세지가 발행되면 "/sub/chat/room/[roomId]"로 메세지가 전송되는 것을 볼 수 있다.
//    Client 에서는 해당 주소를 SUBSCRIBE 하고 있다가 메세지가 전달되면 화면에 출력한다.
//    이때 /sub/chat/room/[roomId]는 채팅방을 구분하는 값이다.
//    기존의 핸들러 ChatHandler 의 역할을 대신 해주므로 핸들러는 없어도 된다.
}
