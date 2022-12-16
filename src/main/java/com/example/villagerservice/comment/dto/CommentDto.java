package com.example.villagerservice.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class CommentDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class CommentRequest {

        private Long score;             //평점
        private String contents;         //후기
        private Long otherId;           // 다른사용자아이디

    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class CommentValidRequest {
        @NotBlank
        @Size(min = 2, message = "두글자 이상 입력해주세요.")
        private String comment;
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class findByAllResponse {   // 전체조회

        private Long totalCount; // 후기 총 갯수
        private List<CommentContentsItemDto> commentContents;

    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class findByNameResponse {   // 전체조회이름조회

        private Long NameSearchTotalCount; //  네임으로 일치된 후기게시글 (토탈 카운트)
        private List<CommentContentsItemDto> commentContents;

    }

}
