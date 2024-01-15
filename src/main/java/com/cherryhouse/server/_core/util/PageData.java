package com.cherryhouse.server._core.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PageData {
    private Long totalDataCnt; //총 데이터 수
    private int totalPages; //총 페이지 수
    private Boolean isLastPage; //마지막 페이지인지
    private Boolean isFirstPage; //첫 페이지인지
    private int requestPage; //요청 페이지 번호
    private int requestSize; //요청 페이지 번호에 있는 데이터 수
}
