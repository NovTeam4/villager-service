package com.example.villagerservice.member.dto;

import com.example.villagerservice.member.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class CreateMemberAttentionTag {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private List<String> tags = new ArrayList<>();
        public List<Tag> toEntity() {
            return tags.stream()
                    .map(Tag::new)
                    .collect(Collectors.toList());
        }
    }
}
