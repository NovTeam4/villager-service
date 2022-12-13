package com.example.villagerservice.common.domain;

import java.util.List;

public abstract class PageResponse {
    private int totalCount;// 총 페이지 개수
    private int page;// 현재 페이지 번호
    private List<?> data;// 데이터
}
