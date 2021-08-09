package com.boutouil.client;

import com.boutouil.shared.CheckoutPayload;
import com.boutouil.shared.CheckoutResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(
        name = "mpgs-checkout",
        url = "${mastercard.gateway.rest-api}",
        configuration = MpgsClientConfiguration.class
)
public interface MpgsServiceClient {

    @PostMapping(value = "/session")
    Optional<CheckoutResponse> createCheckoutSession(@RequestBody CheckoutPayload payload);
}