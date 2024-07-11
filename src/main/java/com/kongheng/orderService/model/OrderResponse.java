package com.kongheng.orderService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
public class OrderResponse {

    private long orderId;

    private Instant orderDate;

    private String orderStatus;

    private long amount;

    private ProductDetail productDetail;

    private PaymentDetail paymentDetail;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDetail {

        private String productName;

        private long productId;

        private long quantity;

        private long price;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentDetail {

        private long paymentId;

        private String status;

        private PaymentMode paymentMode;

        private long amount;

        private Instant paymentDate;

        private long orderId;

    }

}
