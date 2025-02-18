package com.server.delivery.domain.menu.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class MenuRequestDto {
    private String foodName;
    private String description;
    private int price;
    private boolean availability;

}
