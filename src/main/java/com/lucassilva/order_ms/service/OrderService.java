package com.lucassilva.order_ms.service;

import com.lucassilva.order_ms.controller.dto.OrderResponse;
import com.lucassilva.order_ms.entity.OrderEntity;
import com.lucassilva.order_ms.entity.OrderItem;
import com.lucassilva.order_ms.listener.dto.OrderCreatedEvent;
import com.lucassilva.order_ms.repository.OrderRepository;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final MongoTemplate mongoTemplate;

    public OrderService(OrderRepository orderRepository, MongoTemplate mongoTemplate) {
        this.orderRepository = orderRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void save(OrderCreatedEvent event){
        OrderEntity entity = new OrderEntity();
        entity.setOrderId(event.orderCode());
        entity.setCustomerId(event.clientCode());
        entity.setTotal(getTotal(event));
        entity.setItems(getOrderItems(event));

        orderRepository.save(entity);
    }

    public Page<OrderResponse> findAllByCustomerId(Long customerId, PageRequest pageRequest){
        Page<OrderEntity> orders = orderRepository.findAllByCustomerId(customerId, pageRequest);
        return orders.map(OrderResponse::fromEntity);
    }

    public BigDecimal findTotalOnOrdersByCustomerId(Long customerId){
      var aggregations = newAggregation(
              match(Criteria.where("customerId").is(customerId)),
              group().sum("total").as("total")
      );

      var response = mongoTemplate.aggregate(aggregations, "tb_orders", Document.class);
      return new BigDecimal(Objects.requireNonNull(response.getUniqueMappedResult()).get("total").toString());
    };

    private BigDecimal getTotal(OrderCreatedEvent event) {
        return event.items()
                .stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private static List<OrderItem> getOrderItems(OrderCreatedEvent event) {
        return event.items().stream()
                .map(item -> new OrderItem(item.product(), item.quantity(), item.price()))
                .toList();
    }
}
