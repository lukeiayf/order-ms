package com.lucassilva.order_ms.factory;

import com.lucassilva.order_ms.entity.OrderEntity;
import com.lucassilva.order_ms.entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public class OrderEntityFactory {

    public static OrderEntity build() {
        OrderItem items = new OrderItem("notebook", 1, BigDecimal.valueOf(20.50));

        OrderEntity entity = new OrderEntity();
        entity.setOrderId(1L);
        entity.setCustomerId(2L);
        entity.setTotal(BigDecimal.valueOf(20.50));
        entity.setItems(List.of(items));

        return entity;
    }
}
