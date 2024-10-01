package com.lucassilva.order_ms.factory;

import com.lucassilva.order_ms.entity.OrderItem;
import com.lucassilva.order_ms.listener.dto.OrderCreatedEvent;
import com.lucassilva.order_ms.listener.dto.OrderItemEvent;

import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class OrderCreatedEventFactory {

    public static OrderCreatedEvent build(){
        var item = new OrderItemEvent("notebook", 1, BigDecimal.valueOf(20.50));
        var event = new OrderCreatedEvent(2L, 1L, List.of(item));

        return event;
    }
}
