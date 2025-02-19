package com.server.delivery.domain.master.service;

import com.server.delivery.common.PageCustom;
import com.server.delivery.domain.store.dto.response.StoreResponseDto;
import com.server.delivery.domain.store.service.StoreService;
import com.server.delivery.model.master.repository.MasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {
    private final MasterRepository masterRepository;
    private final StoreService storeService;

    @Override
    public PageCustom<StoreResponseDto> getStoreListNeedGrant(Long id, Pageable pageable) {
        Sort defaultSort = Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("modifiedAt"));

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);

        //TODO :: Page<User> userPage = storeRepository.findByNicknameContaining(keyword, sortedPageable);
        return null;
    }

    @Override
    public void approveStore(Long userId, String storeId) {
        //TODO :: Store브랜치에서 StoreId조회하는 공유 메서드 작성하기
//        Store store = storeHelper.getStore(storeId);
    }
}
