package com.kongheng.orderService.service;

import com.kongheng.orderService.entity.Order;
import com.kongheng.orderService.exception.CustomException;
import com.kongheng.orderService.external.client.PaymentService;
import com.kongheng.orderService.external.client.ProductService;
import com.kongheng.orderService.external.request.PaymentRequest;
import com.kongheng.orderService.external.response.ProductResponse;
import com.kongheng.orderService.model.OrderRequest;
import com.kongheng.orderService.model.OrderResponse;
import com.kongheng.orderService.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

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

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new CustomException(
                "Order not found for the Id: " + orderId,
                "NOT_FOUND",
                400));

        ProductResponse productResponse = restTemplate.getForObject(
            "http://product-service/product/" + order.getProductId(),
            ProductResponse.class
        );

        assert productResponse != null;

        OrderResponse.ProductDetail productDetail = OrderResponse.ProductDetail.builder()
            .productName(productResponse.getProductName())
            .productId(productResponse.getProductId())
            .quantity(order.getQuantity())
            .price(productResponse.getPrice())
            .build();

        return OrderResponse.builder()
            .orderId(order.getId())
            .orderStatus(order.getOrderStatus())
            .amount(order.getAmount())
            .orderDate(order.getOrderDate())
            .productDetail(productDetail)
            .build();
    }
}
