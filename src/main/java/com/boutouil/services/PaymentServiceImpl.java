package com.boutouil.services;

import com.boutouil.client.MpgsServiceClient;
import com.boutouil.shared.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.boutouil.shared.Utils.Prefixes.ORDER;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final MpgsServiceClient mpgsServiceClient;

    @Override
    public CheckoutResponse checkoutSession(HandleRequest handleRequest) {
        if (handleRequest.getCost() == 0.0d)
            return null;

        final var orderId = Utils.createUniqueId(ORDER);

        final var payload = CheckoutPayload.builder()
                .orderId(orderId)
                .apiOperation(MpgsApiOperation.CREATE_CHECKOUT_SESSION)
                .amount(String.valueOf(handleRequest.getCost()))
                .build();

        final var checkoutSession = mpgsServiceClient.createCheckoutSession(payload)
                .orElse(null);

        if (Objects.isNull(checkoutSession)
                || !checkoutSession.getResult().equals("SUCCESS")
                || Objects.isNull(checkoutSession.getSuccessIndicator())
        )
            return null;
        return checkoutSession;
    }
}