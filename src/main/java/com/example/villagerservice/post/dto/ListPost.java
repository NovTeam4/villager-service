package com.example.villagerservice.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ListPost {

  /*
   작성자, 조회수 몇분전
   글의제목  댓글수 좋아요수
     */

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static  class Response{

        private String title;
        private String nickname;

    }
}
