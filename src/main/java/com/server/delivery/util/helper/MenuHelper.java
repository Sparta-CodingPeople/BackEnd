package com.server.delivery.util.helper;

import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomMenuException;
import com.server.delivery.model.menu.entity.Menu;
import com.server.delivery.model.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MenuHelper {
    private final MenuRepository menuRepository;

    public Menu getMenu(UUID menuUuid) {
        return menuRepository.findByMenuUuId(menuUuid).orElseThrow(
                () -> new CustomMenuException(ExceptionCode.MENU_NOT_FOUND)

        );
    }

}
