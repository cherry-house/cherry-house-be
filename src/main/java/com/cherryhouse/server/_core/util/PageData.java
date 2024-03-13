package com.cherryhouse.server._core.util;

import org.springframework.data.domain.Page;

public record PageData (
        Long totalDataCnt, //총 데이터 수
        int totalPages, //총 페이지 수
        Boolean isLastPage, //마지막 페이지인지
        Boolean isFirstPage, //첫 페이지인지
        int requestPage, //요청 페이지 번호
        int requestSize //요청 페이지 번호에 있는 데이터 수
){
    public static PageData getPageData(Page<?> postList) {
        return new PageData(
                postList.getTotalElements(),
                postList.getTotalPages(),
                postList.isLast(),
                postList.isFirst(),
                postList.getNumber(),
                postList.getSize()
        );
    }
}

