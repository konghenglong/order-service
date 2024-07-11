package com.kongheng.orderService.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class OrderResponse {

    private long orderId;

    private Instant orderDate;

    private String orderStatus;

    private long amount;

}
