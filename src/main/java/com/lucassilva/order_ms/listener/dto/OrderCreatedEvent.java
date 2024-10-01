package com.lucassilva.order_ms.listener.dto;

import java.util.List;

public record OrderCreatedEvent (
        Long orderCode,
        Long clientCode,
        List<OrderItemEvent> items
){

}
