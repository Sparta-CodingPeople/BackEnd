package com.server.delivery.domain.store.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreLocationUpdateDto {
    private String address;
    private String zipcode;
    private int seoulRegionCode;
}
