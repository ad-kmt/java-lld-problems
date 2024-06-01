package com.kmt.lld.parkinglot.model;

import com.kmt.lld.parkinglot.enums.PaymentMode;
import com.kmt.lld.parkinglot.enums.PaymentStatus;
import lombok.Data;

@Data
public class Payment {
    int id;
    PaymentMode paymentMode;
    PaymentStatus paymentStatus;
}
