package com.server.delivery.domain.store.service;

import com.server.delivery.domain.store.dto.request.StoreLocationRequestDto;
import com.server.delivery.domain.store.dto.request.StoreOperatingHoursRequestDto;
import com.server.delivery.domain.store.dto.request.StoreRegisterRequestDto;
import com.server.delivery.domain.store.dto.request.StoreUpdateRequestDto;
import com.server.delivery.domain.store.dto.response.StoreResponseDto;
import com.server.delivery.model.store.entity.*;
import com.server.delivery.model.store.repository.StoreJpaRepository;
import com.server.delivery.model.store.repository.StoreRepository;
import com.server.delivery.util.helper.UserHelper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreJpaRepository storeJpaRepository;
    private final StoreRepository storeRepository;
    private final UserHelper userHelper;

    @PersistenceContext
    private EntityManager entityManager;

    public void deleteStore(
            UUID id,
            String password) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 매장을 찾을 수 없습니다."));

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
        StoreCategory storeCategory = storeRepository.findByStoreCategoryId(requestDto.getStoreInfo().getStoreCategoryId())
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 매장 카테고리"));

        //서울 지역 코드 조회
        SeoulAreaCode seoulAreaCode = storeRepository.findBySeoulRegionCode(requestDto.getStoreLocation().getSeoulRegionCode())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 서울 지역 코드입니다."));

        // 기존 Location 확인
        Optional<Location> existingLocation = storeRepository.findLocationBySeoulRegionCode(requestDto.getStoreLocation().getSeoulRegionCode());

        Location location = existingLocation.orElseGet(() -> {
            System.out.println("새로운 Location 생성 시작...");

            Location newLocation = Location.builder()
                    .locationCategory(seoulAreaCode)
                    .address(requestDto.getStoreLocation().getAddress())
                    .zipcode(requestDto.getStoreLocation().getZipcode())
                    .build();

            entityManager.persist(newLocation);
            entityManager.flush();

            System.out.println("새로운 Location DB 저장 완료: " + newLocation);
            return newLocation;
        });

        // 기존 StoreLocation 확인
        Optional<StoreLocation> existingStoreLocation = storeRepository.findStoreLocationByLocation(location);

        StoreLocation storeLocation = existingStoreLocation.orElseGet(() -> {
            System.out.println("새로운 StoreLocation 생성 시작...");

            StoreLocation newStoreLocation = StoreLocation.builder()
                    .location(location)
                    .build();

            entityManager.persist(newStoreLocation);
            entityManager.flush();

            System.out.println("새로운 StoreLocation DB 저장 완료: " + newStoreLocation);
            return newStoreLocation;
        });

        //매장 정보 저장
        final Store savedStore = storeRepository.saveStore(Store.builder()
                .storeName(requestDto.getStoreInfo().getStoreName())
                .phoneNumber(requestDto.getStoreInfo().getPhoneNumber())
                .storeDescription(requestDto.getStoreInfo().getStoreDescription())
                .storeCategory(storeCategory)
                .storeLocation(storeLocation)
                .build());

        // 운영시간 저장
        requestDto.getOperatingHours().stream()
                .map(hoursDto -> {
                    if (hoursDto == null) {
                        throw new IllegalArgumentException("OperatingHoursDto 객체가 null입니다.");
                    }


                    OperationTimes operationTimes = OperationTimes.builder()
                            .weekday(convertWeekdayToInt(hoursDto.getWeekday()))
                            .operationTimeOpeningTime(hoursDto.getOperationTimeOpeningTime())
                            .operationTimeClosingTime(hoursDto.getOperationTimeClosingTime())
                            .isHoliday(hoursDto.isHoliday())
                            .build();

                    storeRepository.saveOperationTimes(operationTimes);

                    StoreOperationTimes storeOperationTimes = StoreOperationTimes.builder()
                            .store(savedStore)
                            .operationTimes(operationTimes)
                            .build();

                    storeRepository.saveStoreOperationTimes(storeOperationTimes);

                    return storeOperationTimes;
                }).forEach(storeRepository::saveStoreOperationTimes);
    }

    private int convertWeekdayToInt(List<String> weekdays) {
        Map<String, Integer> weekdayMap = Map.of(
                "SUNDAY", 1, "MONDAY", 2, "TUESDAY", 3, "WEDNESDAY", 4,
                "THURSDAY", 5, "FRIDAY", 6, "SATURDAY", 7
        );

        return weekdays.stream()
                .map(weekdayMap::get)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 요일 값입니다."));
    }
}