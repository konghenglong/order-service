package com.kongheng.orderService.service;

import com.kongheng.orderService.model.OrderRequest;

public interface OrderService {

    long placeOrder(OrderRequest request);

}
