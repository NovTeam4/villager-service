package com.example.villagerservice.party.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PartyChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomId;

    private String roomName;

    private String hostNickname;

    @OneToMany
    private List<PartyChatMessage> partyChatMessageList = new ArrayList<>();

    // 새로운 메세지를 업데이트
    public PartyChatRoom update(PartyChatMessage message) {
        this.partyChatMessageList.add(message);
        return this;
    }
}