package com.boutouil.shared;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
public class CheckoutPayload {

    private String apiOperation;

    private Map<String, String> interaction;

    private Order order;

    @Builder
    public CheckoutPayload(String apiOperation, String orderId, String amount) {
        this.apiOperation = apiOperation;
        this.order = Order.builder()
                .id(orderId)
                .amount(amount)
                .currency("USD")
                .description("Mpgs test payment")
                .build();
        this.interaction = Map.of("operation", "PURCHASE");
    }
}

@Data
@Builder
class Order {

    private String id;

    private String amount;

    private String currency;

    private String description;
}