package com.example.villagerservice.member.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class MemberDetail {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private Long memberId;
        private String nickName;
        private String email;
        private String introduce;
        private String gender;
        private String birth;
        private Integer mannerPoint;
        private Long partyRegisterCount;
        private Long postRegisterCount;
        private Long follow;
        private Long follower;
        private boolean followState;
        private List<String> tags = new ArrayList<>();

        public Response(Long partyRegisterCount, Long postRegisterCount, Long follow, Long follower) {
            this.partyRegisterCount = partyRegisterCount;
            this.postRegisterCount = postRegisterCount;
            this.follow = follow;
            this.follower = follower;
            this.followState = false;
        }

        public void addTags(List<String> tags) {
            this.tags = tags;
        }
    }
}
