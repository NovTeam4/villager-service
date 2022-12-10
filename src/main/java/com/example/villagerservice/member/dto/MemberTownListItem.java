package com.example.villagerservice.member.dto;

import com.example.villagerservice.common.utils.StringConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberTownListItem {
    private Long memberTownId;
    private String name;
    private String townName;
    private String createdAt;
    private String modifiedAt;

    public MemberTownListItem(Long memberTownId, String name, String city, String town, String village,  LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.memberTownId = memberTownId;
        this.name = name;
        this.townName = getTownName(city, town, village);
        this.createdAt = StringConverter.localDateTimeToLocalDateString(createdAt);
        this.modifiedAt = StringConverter.localDateTimeToLocalDateString(modifiedAt);
    }

    private String getTownName(String city, String town, String village) {
        return String.format("%s %s %s", city, town, village);
    }
}
