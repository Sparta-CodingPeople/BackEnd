package com.server.delivery.domain.menu.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.server.delivery.common.PageCustom;
import com.server.delivery.common.exception.customException.CustomMenuException;
import com.server.delivery.common.exception.customException.CustomStoreException;
import com.server.delivery.domain.menu.dto.request.MenuCreateRequestDto;
import com.server.delivery.domain.menu.dto.response.MenuResponseDto;
import com.server.delivery.model.menu.entity.Menu;
import com.server.delivery.model.menu.repository.MenuRepository;
import com.server.delivery.model.store.entity.Store;
import com.server.delivery.model.store.repository.store.StoreRepository;
import com.server.delivery.model.user.entity.User;
import com.server.delivery.model.user.entity.UserStore;
import com.server.delivery.util.helper.UserHelper;
import com.server.delivery.util.s3image.S3ImageUtil;

@ExtendWith(MockitoExtension.class)
class MenuServiceImplTest {

	@Mock
	private StoreRepository storeRepository;

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private S3ImageUtil s3ImageUtil;

	@Mock
	private UserHelper userHelper;

	@InjectMocks
	private MenuServiceImpl menuService;

	private User user;
	private Store store;
	private Menu menu;
	private UUID storeUuid;
	private UUID menuUuid;
	private UserStore userStore;

	@BeforeEach
	void setUp() {
		storeUuid = UUID.randomUUID();
		menuUuid = UUID.randomUUID();

		store = Store.builder().
			storeUuid(storeUuid)
			.build();

		user = User.builder().id(1L).userStores(new ArrayList<>()).build();
		UserStore userStore = UserStore.builder().store(store).user(user).build();
		user.getUserStores().add(userStore);

		menu = Menu.builder().menuUuId(menuUuid).store(store).build();
	}

	@Test
	void testRegisterMenu_Success() {
		// Given
		MenuCreateRequestDto requestDto =
			MenuCreateRequestDto.builder().foodName("Pizza").price(15000).availability(true).build();
		String uploadedImage = "s3://image-path.jpg";

		when(storeRepository.findByStoreUuid(storeUuid)).thenReturn(Optional.of(store));
		when(userHelper.getUserById(user.getId())).thenReturn(user);
		when(s3ImageUtil.uploadImageToS3(any())).thenReturn(uploadedImage);

		// When
		menuService.registerMenu(user.getId(), storeUuid, requestDto, null);

		// Then
		verify(menuRepository, times(1)).save(any(Menu.class));
	}

	@Test
	void testRegisterMenu_StoreNotFound() {
		// Given
		when(storeRepository.findByStoreUuid(storeUuid)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(CustomStoreException.class, () -> {
			menuService.registerMenu(user.getId(), storeUuid, MenuCreateRequestDto.builder().build(), null);
		});
	}

	@Test
	void testGetMenu_Success() {
		// Given
		when(menuRepository.findByMenuUuId(menuUuid)).thenReturn(Optional.of(menu));
		when(userHelper.getUserById(user.getId())).thenReturn(user);

		// When
		MenuResponseDto response = menuService.getMenu(user.getId(), menuUuid);

		// Then
		assertNotNull(response);
		assertEquals(menuUuid, response.getMenuUuid());
	}

	@Test
	void testGetMenu_MenuNotFound() {
		// Given
		when(menuRepository.findByMenuUuId(menuUuid)).thenReturn(Optional.empty());

		// When & Then
		assertThrows(CustomMenuException.class, () -> {
			menuService.getMenu(user.getId(), menuUuid);
		});
	}

	@Test
	void testSearchMenus_Success() {
		// Given
		Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdAt")));
		Page<Menu> menuPage = new PageImpl<>(List.of(menu), pageable, 1);

		when(storeRepository.findByStoreUuid(storeUuid)).thenReturn(Optional.of(store));
		when(menuRepository.findByStoreAndMenuNameContaining(store, "Pizza", pageable))
			.thenReturn(menuPage);

		// When
		PageCustom<MenuResponseDto> result = menuService.searchMenus(user.getId(), storeUuid, "Pizza", pageable);

		// Then
		assertNotNull(result);
		assertEquals(1, result.getContent().size());
	}
}
