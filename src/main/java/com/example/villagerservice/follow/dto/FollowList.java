package com.example.villagerservice.follow.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class FollowList {
    @Getter
    @Builder
    public static class Request {
        @Builder.Default
        private Integer page = 1;
        @Builder.Default
        private Integer size = 10;
        public long getOffset() {
            return (long) (max(1, page) - 1) * size;
        }
    }

    @Getter
    @Builder
    public static class Response {
        private int pageNumber;
        private List<FollowListItem> follows = new ArrayList<>();
    }
}
