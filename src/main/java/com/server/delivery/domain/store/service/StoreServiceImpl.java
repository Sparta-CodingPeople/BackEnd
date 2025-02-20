package com.server.delivery.domain.store.service;

import com.server.delivery.domain.store.dto.request.*;
import com.server.delivery.domain.store.dto.response.StoreResponseDto;
import com.server.delivery.model.store.entity.*;
import com.server.delivery.model.store.repository.StoreRepository;
import com.server.delivery.model.user.entity.User;
import com.server.delivery.util.helper.UserHelper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {


    private final StoreRepository storeRepository;
    private final UserHelper userHelper;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void deleteStore(
            UUID id,
            String password,
            String userName) {
        System.out.println(id);
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 매장을 찾을 수 없습니다."));

        // 사용자 정보 가져오기
        User user = userHelper.getUser(userName);

        // 비밀번호 검증
            if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
/*        // 테스트용 비밀번호 검증
        String correctPassword = "password123"; // 테스트용 하드코딩된 비밀번호
        if (!password.equals(correctPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }*/

        store.setStoreIsDeleted(true);

        storeRepository.save(store);
    }

    @Transactional
    public void updateStore(
            UUID id,
            String userName,
            StoreUpdateRequestDto requestDto) {
        // 매장 정보 가져오기
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 매장을 찾을 수 없습니다."));

        // 사용자 정보 가져오기
        User user = userHelper.getUser(userName);

        if (requestDto.getStoreCategoryId() != 0) {
            StoreCategory storeCategory = storeRepository.findByStoreCategoryId(requestDto.getStoreCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매장 카테고리"));
            store.setStoreCategory(storeCategory);  // 카테고리 ID로 가져온 카테고리 객체 설정
        }

        // 변경할 값 설정
        if (requestDto.getStoreName() != null) {
            store.setStoreName(requestDto.getStoreName());
        }
        if (requestDto.getPhoneNumber() != null) {
            store.setPhoneNumber(requestDto.getPhoneNumber());
        }
        if (requestDto.getStoreDescription() != null) {
            store.setStoreDescription(requestDto.getStoreDescription());
        }

        // 수정된 매장 저장
        storeRepository.save(store);

    }

    public StoreResponseDto getStore(UUID id) {
        // 매장 정보 가져오기, 매장이 삭제되지 않은 상태에서만 조회
        Store store = storeRepository.findByStoreUuidAndStoreIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매장입니다."));


        return StoreResponseDto.builder()
                .status("success")
                .message("매장 단일 조회 성공")
                .storeUuid(store.getStoreUuid())
                .storeName(store.getStoreName())
                .storeCategoryId(store.getStoreCategory().getId())
                .storeLocationId(store.getStoreLocation().getLocation().getLocationUuid())
                .phoneNumber(store.getPhoneNumber())
                .storeIsDeleted(store.isStoreIsDeleted())
                .storeIsGranted(store.isStoreIsGranted())
                .operationTimeOpeningTime(store.getOperatingHours().get(0).getOperationTimes().getOperationTimeOpeningTime())
                .operationTimeClosingTime(store.getOperatingHours().get(0).getOperationTimes().getOperationTimeClosingTime())
                .build();
    }




    @Transactional
    public void updateStoreLocation(UUID storeUuid, StoreLocationUpdateDto updateDto) {

        Store store = storeRepository.findByStoreUuidAndStoreIsDeletedFalse(storeUuid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매장입니다."));

        SeoulAreaCode seoulAreaCode = storeRepository.findBySeoulRegionCode(updateDto.getSeoulRegionCode())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 서울 지역 코드입니다."));


        Optional<Location> existingLocation = storeRepository.findLocationByAddressAndCategory(
                updateDto.getAddress(),
                seoulAreaCode.getSeoulRegionCode()
        );

        Location location = existingLocation.orElseGet(() -> {
            Location newLocation = Location.builder()
                    .locationCategory(seoulAreaCode)
                    .address(updateDto.getAddress())
                    .zipcode(updateDto.getZipcode())
                    .build();
            entityManager.persist(newLocation);
            entityManager.flush();
            return newLocation;
        });

        Optional<StoreLocation> existingStoreLocation = storeRepository.findByLocationUuid(location.getLocationUuid());

        StoreLocation storeLocation = existingStoreLocation.orElseGet(() -> {
            StoreLocation newStoreLocation = StoreLocation.builder()
                    .location(location)
                    .build();
            entityManager.persist(newStoreLocation);
            entityManager.flush();
            return newStoreLocation;
        });


        store.setStoreLocation(storeLocation);
        storeRepository.save(store);
    }

    @Transactional
    public void updateStoreOperatingHours(UUID storeUuid, List<StoreOperatingHoursUpdateDto> updateDto) {

        Store store = storeRepository.findByStoreUuidAndStoreIsDeletedFalse(storeUuid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매장입니다."));


        updateDto.stream()
                .map(hoursDto -> {
                    List<Integer> weekdays = convertWeekdayToInt(hoursDto.getWeekday());
                    String weekdayStr = "{" + weekdays.stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(",")) + "}";

                    UUID operationTimesUuid = storeRepository.findMatchingOperationTimesUuid(
                            storeUuid,
                            weekdayStr,
                            hoursDto.getOperationTimeOpeningTime(),
                            hoursDto.getOperationTimeClosingTime(),
                            hoursDto.isHoliday()
                    ).orElse(UUID.randomUUID());

                    boolean isHoliday = hoursDto.isHoliday();

                    if (storeRepository.existsByOperationTimesUuid(operationTimesUuid)) {
                        storeRepository.updateOperationTimes(
                                operationTimesUuid,
                                weekdayStr,
                                hoursDto.getOperationTimeOpeningTime(),
                                hoursDto.getOperationTimeClosingTime(),
                                isHoliday
                        );
                    } else {
                        storeRepository.insertOperationTimes(
                                operationTimesUuid,
                                weekdayStr,
                                hoursDto.getOperationTimeOpeningTime(),
                                hoursDto.getOperationTimeClosingTime(),
                                isHoliday
                        );
                    }

                    storeRepository.updateStoreOperationTimes(store.getStoreUuid(), operationTimesUuid);

                    return null;
                })
                .collect(Collectors.toList());
    }

    public List<StoreResponseDto> searchStores(
            String search,
            Pageable pageable) {

        Page<Store> storePage = storeRepository.searchStores(search, pageable);
        return storePage.stream()
                .map(StoreResponseDto::fromEntity)
                .collect(Collectors.toList());

    }

    public Long getTotalStores(String search) {
        return storeRepository.countStores(search);
    }

    @Transactional
    public void registerStore(StoreRegisterRequestDto requestDto) {

        StoreCategory storeCategory = storeRepository.findByStoreCategoryId(requestDto.getStoreInfo().getStoreCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 매장 카테고리"));


        SeoulAreaCode seoulAreaCode = storeRepository.findBySeoulRegionCode(requestDto.getStoreLocation().getSeoulRegionCode())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 서울 지역 코드입니다."));

        Optional<Location> existingLocation = storeRepository.findLocationByAddressAndCategory(
                requestDto.getStoreLocation().getAddress(),
                seoulAreaCode.getSeoulRegionCode()
        );

        // 기존 Location이 없으면 새로 생성
        Location location = existingLocation.orElseGet(() -> {
            Location newLocation = Location.builder()
                    .locationCategory(seoulAreaCode)
                    .address(requestDto.getStoreLocation().getAddress())
                    .zipcode(requestDto.getStoreLocation().getZipcode())
                    .build();

            entityManager.persist(newLocation);
            entityManager.flush();
            return newLocation;
        });


        StoreLocation storeLocation = storeRepository.findByLocationUuid(location.getLocationUuid())
                .orElseGet(() -> {

                    StoreLocation newStoreLocation = StoreLocation.builder()
                            .location(location)
                            .build();

                    entityManager.persist(newStoreLocation);
                    entityManager.flush();
                    return newStoreLocation;
                });

        // 매장 정보 저장
        Store savedStore = storeRepository.save(Store.builder()
                .storeName(requestDto.getStoreInfo().getStoreName())
                .phoneNumber(requestDto.getStoreInfo().getPhoneNumber())
                .storeDescription(requestDto.getStoreInfo().getStoreDescription())
                .storeCategory(storeCategory)
                .storeLocation(storeLocation)
                .build());

        // 운영시간 저장
        requestDto.getOperatingHours().forEach(hoursDto -> {
            List<Integer> weekdays = convertWeekdayToInt(hoursDto.getWeekday());


            // PostgreSQL이 인식할 수 있는 배열 형식 ('{2,3,4,5,6}')
            String weekdayStr = "{" + weekdays.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")) + "}";

            UUID operationTimeUuid = UUID.randomUUID();

            storeRepository.insertOperationTimes(
                    operationTimeUuid,
                    weekdayStr,
                    hoursDto.getOperationTimeOpeningTime(),
                    hoursDto.getOperationTimeClosingTime(),
                    hoursDto.isHoliday()
            );

            String weekdayStrForStringToArray = weekdays.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            List<OperationTimes> operationTimesList = storeRepository.findOperationTimes(
                    weekdayStrForStringToArray,
                    hoursDto.getOperationTimeOpeningTime(),
                    hoursDto.getOperationTimeClosingTime(),
                    hoursDto.isHoliday()
            );
            System.out.println(operationTimesList.toString());
            if (operationTimesList.isEmpty()) {
                throw new NoSuchElementException("해당 운영 시간이 존재하지 않습니다.");
            }
            UUID storeOperationTimesUuid = UUID.randomUUID(); // UUID 직접 생성
            OperationTimes savedOperationTimes = operationTimesList.get(0);

            storeRepository.insertStoreOperationTimes(
                    storeOperationTimesUuid,
                    savedStore.getStoreUuid(),
                    savedOperationTimes.getStoreOperatingTimesUuid()
            );
        });

    }


    private List<Integer> convertWeekdayToInt(List<String> weekdays) {
        Map<String, Integer> weekdayMap = Map.of(
                "SUNDAY", 1, "MONDAY", 2, "TUESDAY", 3, "WEDNESDAY", 4,
                "THURSDAY", 5, "FRIDAY", 6, "SATURDAY", 7
        );

        return weekdays.stream()
                .map(weekdayMap::get)
                .collect(Collectors.toList());
    }
}