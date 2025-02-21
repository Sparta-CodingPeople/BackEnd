package com.server.delivery.domain.store.dto.request;

import com.server.delivery.model.store.constant.SeoulAreaCode;
import com.server.delivery.model.store.entity.Location;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreLocationRequestDto {
    private int zipCode;
    private String address;
    private int seoulRegionCode;

    public Location from(StoreLocationRequestDto requestLocationDto) {
        return Location.builder()
                .seoulAreaCode(SeoulAreaCode.fromNumber(requestLocationDto.getSeoulRegionCode()))
                .address(requestLocationDto.getAddress())
                .zipcode(requestLocationDto.getZipCode())
                .build();
    }
}
