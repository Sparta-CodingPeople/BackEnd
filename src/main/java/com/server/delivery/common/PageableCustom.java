package com.server.delivery.common;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class PageableCustom implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final boolean first;
    private final boolean last;
    private final boolean hasNext;
    private final int totalPages;
    private final long totalElements;
    private final int page;
    private final int size;

    public PageableCustom(Page<?> page) {
        this.first = page.isFirst();
        this.last = page.isLast();
        this.hasNext = page.hasNext();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.page = page.getNumber();  // 0-based index 유지
        this.size = page.getSize();
    }

    public PageableCustom(Slice<?> slice) {
        this.first = slice.isFirst();
        this.last = slice.isLast();
        this.hasNext = slice.hasNext();
        this.page = slice.getNumber();  // 0-based index 유지
        this.size = slice.getSize();

        // Slice는 totalElements와 totalPages 정보가 없으므로 기본값을 설정
        this.totalElements = -1;  // 의미 없는 값이지만, Page와의 일관성을 위해 추가
        this.totalPages = -1;      // Slice 기반이면 전체 페이지 개수를 알 수 없음
    }
}
