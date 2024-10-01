package com.lucassilva.order_ms.service;

import com.lucassilva.order_ms.entity.OrderEntity;
import com.lucassilva.order_ms.factory.OrderCreatedEventFactory;
import com.lucassilva.order_ms.factory.OrderEntityFactory;
import com.lucassilva.order_ms.repository.OrderRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    MongoTemplate mongoTemplate;

    @InjectMocks
    OrderService orderService;

    @Captor
    ArgumentCaptor<OrderEntity> orderEntityCaptor;

    @Nested
    class Save {
        @Test
        void shouldCallRepositorySave() {
            var event = OrderCreatedEventFactory.buildWithOneItem();

            orderService.save(event);

            verify(orderRepository, times(1)).save(any());
        }

        @Test
        void shouldMapEventToEntitySuccess() {
            var event = OrderCreatedEventFactory.buildWithOneItem();

            orderService.save(event);

            verify(orderRepository, times(1)).save(orderEntityCaptor.capture());
            var entity = orderEntityCaptor.getValue();

            assertEquals(event.orderCode(), entity.getOrderId());
            assertEquals(event.clientCode(), entity.getCustomerId());
            assertNotNull(entity.getTotal());
            assertEquals(event.items().getFirst().product(), entity.getItems().getFirst().getProduct());
            assertEquals(event.items().getFirst().quantity(), entity.getItems().getFirst().getQuantity());
            assertEquals(event.items().getFirst().price(), entity.getItems().getFirst().getPrice());

        }

        @Test
        void shouldCalculateOrderTotalWithSuccess() {
            var event = OrderCreatedEventFactory.buildWithTwoItems();
            var totalFirstItem = event.items().getFirst().price().multiply(BigDecimal.valueOf(event.items().getFirst().quantity()));
            var totalSecondItem = event.items().getLast().price().multiply(BigDecimal.valueOf(event.items().getLast().quantity()));
            var orderTotal = totalFirstItem.add(totalSecondItem);

            orderService.save(event);

            verify(orderRepository, times(1)).save(orderEntityCaptor.capture());
            var entity = orderEntityCaptor.getValue();

            assertNotNull(entity.getTotal());
            assertEquals(orderTotal, entity.getTotal());

        }
    }

    @Nested
    class FindAllByCustomerId {
        @Test
        void shouldCallRepository() {
            var customerId = 1L;
            var pageRequest = PageRequest.of(0, 10);
            doReturn(OrderEntityFactory.buildWithPage()).when(orderRepository).findAllByCustomerId(eq(customerId), eq(pageRequest));

            var response = orderService.findAllByCustomerId(customerId, pageRequest);

            verify(orderRepository, times(1)).findAllByCustomerId(eq(customerId), eq(pageRequest));
        }

        @Test
        void shouldMapResponse() {
            var customerId = 1L;
            var pageRequest = PageRequest.of(0, 10);
            var page = OrderEntityFactory.buildWithPage();
            doReturn(page).when(orderRepository).findAllByCustomerId(anyLong(), any());

            var response = orderService.findAllByCustomerId(customerId, pageRequest);

            assertEquals(page.getTotalPages(), response.getTotalPages());
            assertEquals(page.getTotalElements(), response.getTotalElements());
            assertEquals(page.getSize(), response.getSize());
            assertEquals(page.getNumber(), response.getNumber());

            assertEquals(page.getContent().getFirst().getOrderId(), response.getContent().getFirst().orderId());
            assertEquals(page.getContent().getFirst().getCustomerId(), response.getContent().getFirst().customerId());
            assertEquals(page.getContent().getFirst().getTotal(), response.getContent().getFirst().total());
        }
    }

}