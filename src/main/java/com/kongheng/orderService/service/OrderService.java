package com.kongheng.orderService.service;

import com.kongheng.orderService.model.OrderRequest;
import com.kongheng.orderService.model.OrderResponse;

public interface OrderService {

    long placeOrder(OrderRequest request);

    OrderResponse getOrderDetails(long orderId);
}
