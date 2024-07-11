package com.kongheng.orderService.service;

import com.kongheng.orderService.entity.Order;
import com.kongheng.orderService.external.client.PaymentService;
import com.kongheng.orderService.external.client.ProductService;
import com.kongheng.orderService.external.request.PaymentRequest;
import com.kongheng.orderService.model.OrderRequest;
import com.kongheng.orderService.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

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

        PaymentRequest paymentRequest = PaymentRequest.builder()
            .orderId(order.getId())
            .paymentMode(request.getPaymentMode())
            .amount(request.getTotalAmount())
            .build();

        String orderStatus;
        try {
            paymentService.doPayment(paymentRequest);
            orderStatus = "PLACED";
        } catch (Exception e) {
            orderStatus = "PAYMENT_FAILED";
            log.error("Error occurred in payment.");
        }

        order.setOrderStatus(orderStatus);

        orderRepository.save(order);

        return order.getId();
    }
}
