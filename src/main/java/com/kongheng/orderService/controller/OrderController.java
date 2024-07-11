package com.kongheng.orderService.controller;

import com.kongheng.orderService.model.OrderRequest;
import com.kongheng.orderService.model.OrderResponse;
import com.kongheng.orderService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok().body(orderService.placeOrder(request));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable long orderId) {
        return ResponseEntity.ok().body(orderService.getOrderDetails(orderId));
    }

}
