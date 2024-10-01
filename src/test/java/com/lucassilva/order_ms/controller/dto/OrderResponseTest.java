package com.lucassilva.order_ms.controller.dto;

import com.lucassilva.order_ms.factory.OrderEntityFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderResponseTest {
    @Nested
    class fromEntity {

        @Test
        void shouldMapCorrectly() {
            var input = OrderEntityFactory.build();

            OrderResponse output = OrderResponse.fromEntity(input);

            assertEquals(input.getOrderId(), output.orderId());
            assertEquals(input.getCustomerId(), output.customerId());
            assertEquals(input.getTotal(), output.total());
        }
    }

}