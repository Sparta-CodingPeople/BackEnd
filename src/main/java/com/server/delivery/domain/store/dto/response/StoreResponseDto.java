package com.server.delivery.domain.store.dto.response;

import com.server.delivery.model.store.entity.OperationTimes;
import com.server.delivery.model.store.entity.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class StoreResponseDto {

    private UUID storeUuid;
    private String storeName;
    private List<String> storeCategoryList;
    private String address;
    private int zipCode;
    private String phoneNumber;
    private boolean storeIsDeleted;
    private boolean storeIsGranted;
    private List<OperationTimesresponseDto> storeOperationList;
    private double reviewsRate;

    public static StoreResponseDto from(Store store, double reviewsRate) {
        List<String> categoryList = new ArrayList<>();
        store.getCategoryMappings().forEach(
                item -> categoryList.add(item.getStoreCategory().getStoreType().getCategoryNameKorean())
        );
        List<OperationTimesresponseDto> operationTimesresponseDtoList = new ArrayList<>();
        store.getOperatingHours().forEach(
                item -> {
                    OperationTimes operationTimes = item.getOperationTimes();
                    OperationTimesresponseDto operationTimesresponseDto = OperationTimesresponseDto.builder()
                            .days(operationTimes.getWeekday().getKoreanName())
                            .openTime(operationTimes.getOperationTimeOpeningTime())
                            .closeTime(operationTimes.getOperationTimeClosingTime())
                            .isHoliday(operationTimes.isHoliday())
                            .build();
                    operationTimesresponseDtoList.add(operationTimesresponseDto);
                }
        );

        return StoreResponseDto.builder()
                .storeUuid(store.getStoreUuid())
                .storeName(store.getStoreName())
                .storeCategoryList(categoryList)
                .storeIsDeleted(store.isStoreIsDeleted())
                .phoneNumber(store.getPhoneNumber())
                .storeIsGranted(store.isStoreIsGranted())
                .storeOperationList(operationTimesresponseDtoList)
                .address(store.getLocation().getAddress())
                .zipCode(store.getLocation().getZipcode())
                .reviewsRate(reviewsRate)
                .build();

    }

    public static StoreResponseDto from(Store store) {
        List<String> categoryList = new ArrayList<>();
        store.getCategoryMappings().stream().forEach(
                item -> categoryList.add(item.getStoreCategory().getStoreType().getCategoryNameKorean())
        );
        List<OperationTimesresponseDto> operationTimesresponseDtoList = new ArrayList<>();
        store.getOperatingHours().stream().forEach(
                item -> {
                    OperationTimes operationTimes = item.getOperationTimes();
                    OperationTimesresponseDto operationTimesresponseDto = OperationTimesresponseDto.builder()
                            .days(operationTimes.getWeekday().getKoreanName())
                            .openTime(operationTimes.getOperationTimeOpeningTime())
                            .closeTime(operationTimes.getOperationTimeClosingTime())
                            .isHoliday(operationTimes.isHoliday())
                            .build();
                    operationTimesresponseDtoList.add(operationTimesresponseDto);
                }
        );

        return StoreResponseDto.builder()
                .storeUuid(store.getStoreUuid())
                .storeName(store.getStoreName())
                .storeCategoryList(categoryList)
                .storeIsDeleted(store.isStoreIsDeleted())
                .phoneNumber(store.getPhoneNumber())
                .storeIsGranted(store.isStoreIsGranted())
                .storeOperationList(operationTimesresponseDtoList)
                .address(store.getLocation().getAddress())
                .zipCode(store.getLocation().getZipcode())
                .build();

    }
}
