package com.server.delivery.common;

import lombok.Getter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.io.Serializable;
import java.util.List;

@Getter
public class PageCustom<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<T> content;

    private PageableCustom pageableCustom;

    public PageCustom(List<T> content, Pageable pageable, Long total) {
        this.content = content;
        this.pageableCustom = new PageableCustom(new PageImpl<T>(content, pageable, total));
    }

    public PageCustom(List<T> content, Pageable pageable, boolean hasNext) {
        this.content = content;
        this.pageableCustom = new PageableCustom(new SliceImpl<T>(content, pageable, hasNext));
    }

}
