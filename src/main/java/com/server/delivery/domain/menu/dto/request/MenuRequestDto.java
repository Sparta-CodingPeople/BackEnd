package com.server.delivery.domain.menu.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class MenuRequestDto {
    private String foodName;
    private String description;
    private int price;
    private boolean availability;
    private MultipartFile foodImage;
}
