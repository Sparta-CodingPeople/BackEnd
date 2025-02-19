package com.server.delivery.domain.store.service;

import com.server.delivery.domain.store.dto.request.StoreLocationRequestDto;
import com.server.delivery.domain.store.dto.request.StoreOperatingHoursRequestDto;
import com.server.delivery.domain.store.dto.request.StoreRegisterRequestDto;
import com.server.delivery.domain.store.dto.request.StoreUpdateRequestDto;
import com.server.delivery.domain.store.dto.response.StoreResponseDto;
import com.server.delivery.model.store.entity.*;
import com.server.delivery.model.store.repository.StoreJpaRepository;
import com.server.delivery.model.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreJpaRepository storeJpaRepository;
    private final StoreRepository storeRepository;

    public void deleteStore(
            UUID id,
            String password) {
    }

    public void updateStore(
            UUID id,
            StoreUpdateRequestDto requestDto) {

    }

    public StoreResponseDto getStore(UUID id) {

        return null;
    }

    public void updateStoreLocation(
            UUID id,
            StoreLocationRequestDto requestDto) {

    }

    public void updateStoreOperatingHours(
            UUID id,
            List<StoreOperatingHoursRequestDto> requestDto) {

    }

    public List<StoreResponseDto> searchStores(
            String search,
            Pageable pageable) {
        return null;
    }

    public Long getTotalStores(String search) {
        return null;
    }

    @Override
    @Transactional
    public void registerStore(StoreRegisterRequestDto requestDto) {
        //매장 카테고리 조회
        StoreCategory storeCategory = storeRepository.findCategoryById(requestDto.getStoreInfo().getStoreCategoryId())
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 매장 카테고리"));


        // 매장 위치 저장
        SeoulAreaCode seoulAreaCode = SeoulAreaCode.builder()
                .seoulRegionCode(requestDto.getStoreLocation().getSeoulRegionCode()) //Id설정
                .build();

        StoreLocation storeLocation = StoreLocation.builder()
                .seoulRegionCode(seoulAreaCode)
                .build();

        //매장 정보 저장
        final Store savedStore = storeRepository.saveStore(Store.builder()
                .storeName(requestDto.getStoreInfo().getStoreName())
                .phoneNumber(requestDto.getStoreInfo().getPhoneNumber())
                .storeDescription(requestDto.getStoreInfo().getStoreDescription())
                .storeCategory(storeCategory)
                .storeLocation(storeLocation)
                .build());

        //운영시간 저장
        requestDto.getOperatingHours().stream()
                .map(hoursDto -> {
                    OperationTimes operationTimes = OperationTimes.builder()
                            .weekday(convertWeekdayToInt(hoursDto.getWeekdays()))
                            .operationTimeOpeningTime(hoursDto.getOperationTimeOpeningTime())
                            .operationTimeClosingTime(hoursDto.getOperationTimeClosingTime())
                            .isHoliday(hoursDto.isStoreIsClosed())
                            .build();

                    StoreOperationTimes storeOperationTimes = StoreOperationTimes.builder()
                            .store(savedStore)
                            .operationTimes(operationTimes)
                            .build();

                    storeRepository.saveStoreOperationTimes(storeOperationTimes); // ✅ 저장
                    return storeOperationTimes;
                })
                .forEach(storeOperationTimes -> {});
    }

    private int convertWeekdayToInt(List<String> weekdays) {
        Map<String, Integer> weekdayMap = Map.of(
                "SUNDAY", 0, "MONDAY", 1, "TUESDAY", 2, "WEDNESDAY", 3,
                "THURSDAY", 4, "FRIDAY", 5, "SATURDAY", 6
        );

        return weekdays.stream()
                .map(weekdayMap::get)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 요일 값입니다."));
    }
}