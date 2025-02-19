package com.server.delivery.domain.store.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreLocationRequestDto {
    private String address;
    private String zipcode;
    private int seoulRegionCode;
}
