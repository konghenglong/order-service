package com.kongheng.orderService.external.request;

import com.kongheng.orderService.model.PaymentMode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequest {

    private long orderId;

    private long amount;

    private String referenceNumber;

    private PaymentMode paymentMode;

}
