package com.server.delivery.model.store.repository.store;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.server.delivery.model.store.constant.SeoulAreaCode;
import com.server.delivery.model.store.entity.QLocation;
import com.server.delivery.model.store.entity.QStore;
import com.server.delivery.model.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomStoreRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Store> searchStoresByArea(String search, Pageable pageable) {
        QStore store = QStore.store;
        QLocation location = QLocation.location;

        // 페이지 크기 제한 적용 (10, 30, 50만 허용)
        int size = pageable.getPageSize();
        if (size != 10 && size != 30 && size != 50) {
            size = 10; // 기본값 설정
        }

        // 기본 정렬 설정 (modifiedAt → createdAt 순으로 내림차순 정렬)
        Sort defaultSort = Sort.by(Sort.Direction.DESC, "modifiedAt", "createdAt");

        // 수정된 Pageable 객체 생성
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), size, defaultSort);

        // 검색 조건에 맞는 Store 목록 조회
        List<Store> results = queryFactory
                .selectFrom(store)
                .leftJoin(store.location, location)
                .where(addressContains(search).or(areaCodeEquals(search)))
                .offset(sortedPageable.getOffset())
                .limit(sortedPageable.getPageSize())
                .fetch();

        // 전체 Store 개수 조회
        long total = Optional.ofNullable(queryFactory
                .select(store.count())
                .from(store)
                .leftJoin(store.location, location)
                .where(addressContains(search).or(areaCodeEquals(search)))
                .fetchOne()).orElse(0L);

        return PageableExecutionUtils.getPage(results, pageable, () -> total);
    }

    private BooleanExpression addressContains(String search) {
        return (search != null) ? QLocation.location.address.containsIgnoreCase(search) : null;
    }

    private BooleanExpression areaCodeEquals(String search) {
        if (search == null) return null;
        try {
            SeoulAreaCode areaCode = SeoulAreaCode.valueOf(search.toUpperCase());
            return QLocation.location.seoulAreaCode.eq(areaCode);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}