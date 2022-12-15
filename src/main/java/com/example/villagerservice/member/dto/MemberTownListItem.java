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
    private String townName;
    private String cityName;
    private String createdAt;
    private String modifiedAt;
    private boolean isMain;

    public MemberTownListItem(Long memberTownId, String townName, String city, String town, String village,  LocalDateTime createdAt, LocalDateTime modifiedAt, boolean isMain) {
        this.memberTownId = memberTownId;
        this.townName = townName;
        this.cityName = getCityName(city, town, village);
        this.createdAt = StringConverter.localDateTimeToLocalDateTimeString(createdAt);
        this.modifiedAt = StringConverter.localDateTimeToLocalDateTimeString(modifiedAt);
        this.isMain = isMain;
    }

    private String getCityName(String city, String town, String village) {
        return String.format("%s %s %s", city, town, village);
    }
}
