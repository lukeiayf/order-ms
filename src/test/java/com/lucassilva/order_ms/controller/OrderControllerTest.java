package com.lucassilva.order_ms.controller;

import com.lucassilva.order_ms.controller.dto.ApiResponse;
import com.lucassilva.order_ms.controller.dto.OrderResponse;
import com.lucassilva.order_ms.factory.OrderResponseFactory;
import com.lucassilva.order_ms.service.OrderService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    @Mock
    OrderService orderService;

    @InjectMocks
    OrderController orderController;

    @Captor
    ArgumentCaptor<Long> customerIdCaptor;

    @Captor
    ArgumentCaptor<PageRequest> pageRequestCaptor;

    @Nested
    class ListOrders {

        @Test
        void shouldReturnHttpOk() {
            var customerId = 1L;
            var page = 0;
            var size = 10;
            doReturn(OrderResponseFactory.buildWithOneItem()).when(orderService).findAllByCustomerId(anyLong(), any());
            doReturn(BigDecimal.valueOf(20.50)).when(orderService).findTotalOnOrdersByCustomerId(anyLong());

            ResponseEntity<ApiResponse<OrderResponse>> response = orderController.listOrders(customerId, page, size);

            assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        }

        @Test
        void shouldPassCorrectParametersToService() {
            var customerId = 1L;
            var page = 0;
            var size = 10;
            doReturn(OrderResponseFactory.buildWithOneItem()).when(orderService).findAllByCustomerId(customerIdCaptor.capture(), pageRequestCaptor.capture());
            doReturn(BigDecimal.valueOf(20.50)).when(orderService).findTotalOnOrdersByCustomerId(customerIdCaptor.capture());

            ResponseEntity<ApiResponse<OrderResponse>> response = orderController.listOrders(customerId, page, size);

            assertEquals(2, customerIdCaptor.getAllValues().size());
            assertEquals(customerId, customerIdCaptor.getAllValues().getFirst());
            assertEquals(customerId, customerIdCaptor.getAllValues().get(1));
            assertEquals(page, pageRequestCaptor.getValue().getPageNumber());
            assertEquals(size, pageRequestCaptor.getValue().getPageSize());
        }

        @Test
        void shouldReturnResponseBodyCorrectly() {
            var customerId = 1L;
            var page = 0;
            var size = 10;
            BigDecimal totalOnOrders = BigDecimal.valueOf(20.50);
            Page<OrderResponse> pagination = OrderResponseFactory.buildWithOneItem();

            doReturn(pagination).when(orderService).findAllByCustomerId(anyLong(), any());
            doReturn(totalOnOrders).when(orderService).findTotalOnOrdersByCustomerId(anyLong());

            ResponseEntity<ApiResponse<OrderResponse>> response = orderController.listOrders(customerId, page, size);

            assertNotNull(response);
            assertNotNull(response.getBody());
            assertNotNull(response.getBody().data());
            assertNotNull(response.getBody().pagination());
            assertNotNull(response.getBody().summary());

            assertEquals(totalOnOrders, response.getBody().summary().get("totalOnOrders"));
            assertEquals(pagination.getTotalElements(), response.getBody().pagination().totalItems());
            assertEquals(pagination.getTotalPages(), response.getBody().pagination().totalPages());
            assertEquals(pagination.getNumber(), response.getBody().pagination().page());
            assertEquals(pagination.getSize(), response.getBody().pagination().size());

            assertEquals(pagination.getContent(), response.getBody().data());
        }
    }
}