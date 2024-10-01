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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
            doReturn(OrderResponseFactory.buildWithOneItem()).when(orderService).findAllByCustomerId(customerIdCaptor.capture(), any());
            doReturn(BigDecimal.valueOf(20.50)).when(orderService).findTotalOnOrdersByCustomerId(customerIdCaptor.capture());

            ResponseEntity<ApiResponse<OrderResponse>> response = orderController.listOrders(customerId, page, size);

            assertEquals(2, customerIdCaptor.getAllValues().size());
            assertEquals(customerId, customerIdCaptor.getAllValues().getFirst());
            assertEquals(customerId, customerIdCaptor.getAllValues().get(1));
        }

        @Test
        void shouldReturnResponseBodyCorrectly() {
        }
    }
}