package com.example.villagerservice.post.dto;

import com.example.villagerservice.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;

public class CreatePost {

    @Data
    @AllArgsConstructor
    public static class Request {

        private Member member;
        private String title;
        private String contetns;

    }

    public static class Response{

    }

}
