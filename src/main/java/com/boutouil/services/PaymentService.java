package com.boutouil.services;


import com.boutouil.shared.CheckoutResponse;
import com.boutouil.shared.HandleRequest;

public interface PaymentService {

    CheckoutResponse checkoutSession(HandleRequest handleRequest);
}