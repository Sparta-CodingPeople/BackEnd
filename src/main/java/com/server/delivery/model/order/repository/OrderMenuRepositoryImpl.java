package com.server.delivery.model.order.repository;

import com.server.delivery.model.menu.entity.Menu;
import com.server.delivery.model.order.entity.OrderMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class OrderMenuRepositoryImpl implements OrderMenuRepository {
    private final OrderMenuJpaRepository jpaRepository;

    @Override
    public OrderMenu save(OrderMenu orderMenu) {
        return jpaRepository.save(orderMenu);
    }

    @Override
    public Optional<OrderMenu> findById(UUID menuId) {
        return jpaRepository.findById(menuId);
    }

    @Override
    public void delete(OrderMenu orderMenu) {
        jpaRepository.delete(orderMenu);
    }

    @Override
    public List<OrderMenu> saveAll(List<OrderMenu> orderMenuList) {
        return jpaRepository.saveAll(orderMenuList);
    }

    @Override
    public boolean isExistMenu(Menu menu) {
        return jpaRepository.existsByMenu(menu);
    }


}
