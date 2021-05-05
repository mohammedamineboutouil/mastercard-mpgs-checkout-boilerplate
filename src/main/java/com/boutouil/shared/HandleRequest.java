package com.boutouil.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HandleRequest implements Serializable {

    private static final long serialVersionUID = -3431076329368124609L;

    private double cost;
}