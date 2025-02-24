package com.server.delivery.domain.delivery.service;

import com.server.delivery.common.exception.ExceptionCode;
import com.server.delivery.common.exception.customException.CustomDeliveryException;
import com.server.delivery.common.exception.customException.CustomOrderException;
import com.server.delivery.common.pagination.PageSize;
import com.server.delivery.domain.delivery.dto.req.DeliveryCompleteRequestDto;
import com.server.delivery.domain.delivery.dto.req.DeliveryStartRequestDto;
import com.server.delivery.domain.delivery.dto.res.DeliveryCompleteResponseDto;
import com.server.delivery.domain.delivery.dto.res.DeliverySearchResponseDto;
import com.server.delivery.domain.delivery.dto.res.DeliveryStartResponseDto;
import com.server.delivery.domain.delivery.repository.DeliveryJpaRepository;
import com.server.delivery.model.delivery.entity.Delivery;
import com.server.delivery.model.delivery.entity.DeliveryStatus;
import com.server.delivery.model.order.entity.Order;
import com.server.delivery.util.helper.DeliveryHelper;
import com.server.delivery.util.helper.OrderHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final OrderHelper orderHelper;
    private final DeliveryHelper deliveryHelper;
    private final DeliveryJpaRepository deliveryJpaRepository;

    @Transactional
    public DeliveryStartResponseDto startDelivery(UUID deliveryId, DeliveryStartRequestDto request) {
        Delivery foundDelivery = deliveryHelper.getDelivery(deliveryId);

        if (DeliveryStatus.isAlreadyStarted(foundDelivery.getStatus())) {
            throw new CustomDeliveryException(ExceptionCode.DELIVERY_ALREADY_START);
        }

        foundDelivery.changeStatusToDelivering();

        Order foundOrder = orderHelper.getOrder(request.orderUuid());
        validateOrderDelivery(foundDelivery, foundOrder);

        return DeliveryStartResponseDto.from(foundDelivery, foundOrder);
    }

    @Transactional
    public DeliveryCompleteResponseDto completeDelivery(UUID deliveryId, DeliveryCompleteRequestDto request) {
        Delivery foundDelivery = deliveryHelper.getDelivery(deliveryId);


        if (DeliveryStatus.isNotDelivering(foundDelivery.getStatus())) {
            throw new CustomDeliveryException(ExceptionCode.DELIVERY_ALREADY_COMPLETED);
        }

        if (DeliveryStatus.isCompletedDelivery(foundDelivery.getStatus())) {
            throw new CustomDeliveryException(ExceptionCode.DELIVERY_ALREADY_DELIVERED);
        }

        foundDelivery.changeStatusToCompleted();

        Order foundOrder = orderHelper.getOrder(request.orderUuid());
        validateOrderDelivery(foundDelivery, foundOrder);

        return DeliveryCompleteResponseDto.from(foundDelivery, foundOrder);
    }

    @Transactional(readOnly = true)
    public DeliverySearchResponseDto searchDelivery(UUID deliveryId) {
        Delivery foundDelivery = deliveryHelper.getDelivery(deliveryId);
        Order foundOrder = orderHelper.getOrder(foundDelivery.getOrder().getOrderUuid());
        return DeliverySearchResponseDto.of(foundOrder, foundDelivery);
    }

    @Transactional(readOnly = true)
    public PagedModel<DeliverySearchResponseDto> searchDeliveries(String keyword, Pageable pageable) {
        Pageable validatedPageable = toPageable(pageable);
        Page<Delivery> deliveries = deliveryJpaRepository.searchDeliveries(keyword, validatedPageable);
        Page<DeliverySearchResponseDto> content = deliveries.map(d -> DeliverySearchResponseDto.of(d.getOrder(), d));
        return new PagedModel<>(content);
    }

    private void validateOrderDelivery(Delivery delivery, Order order) {
        if (!delivery.getOrder().getOrderUuid().equals(order.getOrderUuid())) {
            throw new CustomOrderException(ExceptionCode.ORDER_NOT_FOUND);
        }
    }

    private Pageable toPageable(Pageable originPageable) {
        int validatedSize = PageSize.of(originPageable.getPageSize());
        return PageRequest.of(originPageable.getPageNumber(), validatedSize, originPageable.getSort());
    }
}
