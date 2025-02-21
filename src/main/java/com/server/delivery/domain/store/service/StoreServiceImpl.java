package com.server.delivery.domain.store.service;

import com.server.delivery.common.PageCustom;
import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomStoreException;
import com.server.delivery.common.exception.customException.CustomUserException;
import com.server.delivery.domain.store.dto.request.StoreLocationRequestDto;
import com.server.delivery.domain.store.dto.request.StoreOperatingHoursRequestDto;
import com.server.delivery.domain.store.dto.request.StoreRegisterRequestDto;
import com.server.delivery.domain.store.dto.request.StoreUpdateRequestDto;
import com.server.delivery.domain.store.dto.response.StoreResponseDto;
import com.server.delivery.model.owner.entity.Owner;
import com.server.delivery.model.owner.entity.OwnerStore;
import com.server.delivery.model.owner.repository.OwnerRepository;
import com.server.delivery.model.store.constant.SeoulAreaCode;
import com.server.delivery.model.store.constant.StoreType;
import com.server.delivery.model.store.constant.WeekDays;
import com.server.delivery.model.store.entity.*;
import com.server.delivery.model.store.repository.location.LocationRepository;
import com.server.delivery.model.store.repository.operationTimes.OperationTimesRepository;
import com.server.delivery.model.store.repository.ownerStore.OwnerStoreRepository;
import com.server.delivery.model.store.repository.store.StoreRepository;
import com.server.delivery.model.store.repository.storeCategory.StoreCategoryRepository;
import com.server.delivery.model.store.repository.storeCategoryMapping.StoreCategoryMappingRepository;
import com.server.delivery.model.store.repository.storeOperationTimes.StoreOperationTimesRepository;
import com.server.delivery.model.store.repository.userStore.UserStoreRepository;
import com.server.delivery.model.user.entity.User;
import com.server.delivery.model.user.entity.UserStore;
import com.server.delivery.util.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final LocationRepository locationRepository;
    private final OperationTimesRepository operationTimesRepository;
    private final StoreOperationTimesRepository storeOperationTimesRepository;
    private final StoreCategoryRepository storeCategoryRepository;
    private final UserStoreRepository userStoreRepository;
    private final StoreCategoryMappingRepository storeCategoryMappingRepository;
    private final OwnerRepository ownerRepository;
    private final OwnerStoreRepository ownerStoreRepository;
    private final UserHelper userHelper;

    private static void isStoreGranted(Store store) {
        if (!store.isStoreIsGranted()) {
            throw new CustomStoreException(ExceptionCode.STORE_NOT_GRANTED);
        }
    }

    public void registerStore(
            Long userId,
            StoreRegisterRequestDto requestDto
    ) {
        User user = userHelper.getUserById(userId);


        boolean isExists = storeRepository.existsStoreByStoreName(requestDto.getStoreName());
        if (isExists) {
            throw new CustomStoreException(ExceptionCode.STORE_IS_EXIST);
        }
        // 여러 명의 Owner를 가져오는 로직
        List<Owner> owners = ownerRepository.findByUser(user)
                .orElseThrow(() -> new CustomUserException(ExceptionCode.OWNER_NOT_FOUND));

        // Store 생성 (필수 필드 포함)
        Store store = Store.builder()
                .storeName(requestDto.getStoreName())
                .phoneNumber(requestDto.getPhoneNumber())
                .storeDescription(requestDto.getStoreDescription())
                .storeIsDeleted(false)
                .storeIsGranted(false)
                .build();
        storeRepository.save(store);

        // UserStore 저장
        UserStore userStore = UserStore.builder().store(store).user(user).build();
        userStoreRepository.save(userStore);

        owners.forEach(owner -> {
            OwnerStore ownerStore = OwnerStore.builder()
                    .store(store)  // 현재 store와 연결
                    .owner(owner)  // 현재 owner와 연결
                    .build();
            ownerStoreRepository.save(ownerStore);  // OwnerStore를 DB에 저장
        });

        // 위치 저장
        StoreLocationRequestDto requestLocationDto = requestDto.getStoreLocation();
        Location location = requestLocationDto.from(requestLocationDto);
        Location savedLocation = locationRepository.save(location);

        // 운영 시간 저장
        List<OperationTimes> operationTimesList = requestDto.getOperatingHours().stream()
                .map(StoreOperatingHoursRequestDto::from)
                .toList();
        List<OperationTimes> savedOperationTimesList = operationTimesRepository.saveAll(operationTimesList);

        List<StoreOperationTimes> storeOperationTimesList = savedOperationTimesList.stream()
                .map(operationTimes -> StoreOperationTimes.builder()
                        .store(store)
                        .operationTimes(operationTimes)
                        .build()
                ).toList();

        storeOperationTimesRepository.saveAll(storeOperationTimesList);

        // 카테고리 저장
        List<StoreCategory> storeCategoryList = requestDto.getStoreCategoryId().stream()
                .map(
                        category -> StoreCategory.builder()
                                .storeType(StoreType.fromNumber(category))
                                .build()
                ).toList();
        List<StoreCategory> savedStoreCategory = storeCategoryRepository.saveAll(storeCategoryList);

        // StoreCategoryMapping 생성 및 저장
        List<StoreCategoryMapping> storeCategoryMappingList = savedStoreCategory.stream().map(
                category -> StoreCategoryMapping.builder()
                        .store(store)
                        .storeCategory(category)
                        .build()
        ).toList();
        List<StoreCategoryMapping> categoryMappingList = storeCategoryMappingRepository.saveAll(storeCategoryMappingList);

        // Store 엔티티에 추가 정보 업데이트 후 저장
        store.setLocation(savedLocation);
        store.setOperatingHours(storeOperationTimesList);
        store.setCategoryMappings(categoryMappingList);
        storeRepository.save(store);
    }

    @Transactional
    public void updateStore(
            Long userId,
            UUID storeUuid,
            StoreUpdateRequestDto requestDto
    ) {
        Store store = getStore(storeUuid);

        store.setStoreName(requestDto.getStoreName());
        store.setStoreDescription(requestDto.getStoreDescription());
        store.setStoreDescription(requestDto.getStoreDescription());

        store.getCategoryMappings().forEach(mapping -> mapping.setDeleted(true));

        storeCategoryMappingRepository.saveAll(store.getCategoryMappings());

        List<StoreCategory> storeCategoryList = requestDto.getStoreCategoryId().stream()
                .map(
                        category -> StoreCategory.builder()
                                .storeType(StoreType.fromNumber(category))
                                .build()
                ).toList();

        List<StoreCategory> savedStoreCategory = storeCategoryRepository.saveAll(storeCategoryList);

        //  StoreCategoryMapping 생성 및 저장
        List<StoreCategoryMapping> storeCategoryMappingList = savedStoreCategory.stream().map(
                category -> StoreCategoryMapping.builder()
                        .store(store)
                        .storeCategory(category)
                        .build()
        ).toList();
        List<StoreCategoryMapping> categoryMappingList = storeCategoryMappingRepository.saveAll(storeCategoryMappingList);
        store.getCategoryMappings().addAll(categoryMappingList);
    }

    @Transactional(readOnly = true)
    public PageCustom<StoreResponseDto> searchStores(
            String keyword,
            Pageable pageable) {

        Sort defaultSort = Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("modifiedAt"));

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), defaultSort);

        Page<Store> storePage = storeRepository.findByStoreNameContainingAndStoreIsGrantedTrue(keyword, sortedPageable);

        List<StoreResponseDto> storeList = storePage.getContent().stream()
                .map(StoreResponseDto::from)
                .toList();

        return new PageCustom<>(storeList, sortedPageable, storePage.getTotalElements());
    }

    @Transactional
    public void updateStoreLocation(
            UUID storeUuid,
            StoreLocationRequestDto requestDto
    ) {
        Store store = getStore(storeUuid);
        Location location = store.getLocation();

        location.setAddress(requestDto.getAddress());
        location.setZipcode(requestDto.getZipCode());
        location.setSeoulAreaCode(SeoulAreaCode.fromNumber(requestDto.getSeoulRegionCode()));
        store.setLocation(location);
        storeRepository.save(store);
    }

    @Transactional
    public void updateStoreOperatingHours(
            UUID storeUuid,
            List<StoreOperatingHoursRequestDto> requestDto) {
        Store store = getStore(storeUuid);
        List<StoreOperationTimes> operatingHours = store.getOperatingHours();

        // 요청받은 운영 시간 리스트 처리
        requestDto.forEach(dto ->
                operatingHours.stream()
                        .filter(storeOperationTimes -> storeOperationTimes.getOperationTimes().getWeekday() == WeekDays.fromNumber(dto.getWeekdays()))
                        .findFirst()
                        .ifPresent(storeOperationTimes -> {
                            // 일치하는 weekdays가 있으면 해당 엔티티 수정
                            storeOperationTimes.getOperationTimes().setOperationTimeOpeningTime(dto.getOperationTimeOpeningTime());
                            storeOperationTimes.getOperationTimes().setOperationTimeClosingTime(dto.getOperationTimeClosingTime());
                            storeOperationTimes.getOperationTimes().setHoliday(dto.isHoliday());
                        })
        );
    }

    @Transactional(readOnly = true)
    public StoreResponseDto findStore(UUID storeUuid) {
        Store store = getStore(storeUuid);
        isStoreGranted(store);

        return StoreResponseDto.from(store);
    }

    @Transactional
    public void deleteStore(
            UUID storeUuid,
            String password) {
        //store와 연관된 모든걸 isDeleted변경해야함
        // store조회가 안되면 자동으로 예외가 발생하기 때문에 다른건 softDelete처리하지 않는다? -> 이후 SpringBatch 돌릴대 Store를 삭제하면서 연관된것들을 삭제하면 되기 때문에 ㄱㅊ다고 생각
        Store store = getStore(storeUuid);

        store.softDelete();
    }

    private Store getStore(UUID storeUuid) {
        return storeRepository.findByStoreUuid(storeUuid).orElseThrow(
                () -> new CustomStoreException(ExceptionCode.STORE_NOT_FOUND)
        );
    }
}
