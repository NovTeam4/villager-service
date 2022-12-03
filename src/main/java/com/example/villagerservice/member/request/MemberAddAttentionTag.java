package com.example.villagerservice.member.request;

import com.example.villagerservice.member.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberAddAttentionTag {
    private List<String> tags = new ArrayList<>();
    public List<Tag> toEntity() {
        return tags.stream()
                .map(Tag::new)
                .collect(Collectors.toList());
    }
}
