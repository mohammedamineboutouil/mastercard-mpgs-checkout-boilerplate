package com.boutouil.shared;

import lombok.Data;

@Data
public class CheckoutResponse {

    public String merchant;

    public String result;

    public String successIndicator;

    public CheckoutSession session;
}

@Data
class CheckoutSession {

    public String id;

    public String updateStatus;

    public String version;
}