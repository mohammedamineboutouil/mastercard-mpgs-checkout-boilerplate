package com.boutouil.controller;

import com.boutouil.services.PaymentService;
import com.boutouil.shared.HandleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/checkout")
    public ResponseEntity<Object> handleCheckout(@RequestBody HandleRequest handleRequest) {
        return ResponseEntity.ok(paymentService.checkoutSession(handleRequest));
    }
}
