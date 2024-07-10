package com.kongheng.orderService.service;

import com.kongheng.orderService.entity.Order;
import com.kongheng.orderService.model.OrderRequest;
import com.kongheng.orderService.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public long placeOrder(OrderRequest request) {
        Order order = Order.builder()
            .amount(request.getTotalAmount())
            .orderStatus("CREATED")
            .productId(request.getProductId())
            .orderDate(Instant.now())
            .quantity(request.getQuantity())
            .build();
        order = orderRepository.save(order);
        return order.getId();
    }
}
