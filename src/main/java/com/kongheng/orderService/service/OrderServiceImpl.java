package com.kongheng.orderService.service;

import com.kongheng.orderService.entity.Order;
import com.kongheng.orderService.external.client.ProductService;
import com.kongheng.orderService.model.OrderRequest;
import com.kongheng.orderService.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Override
    public long placeOrder(OrderRequest request) {

        productService.reduceQuantity(request.getProductId(), request.getQuantity());

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
