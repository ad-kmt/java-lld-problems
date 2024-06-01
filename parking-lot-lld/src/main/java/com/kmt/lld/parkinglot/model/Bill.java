package com.kmt.lld.parkinglot.model;

import com.kmt.lld.parkinglot.enums.BillStatus;
import lombok.Data;

import java.util.List;

@Data
public class Bill {
    int id;
    Ticket ticket;
    double amount;
    List<Payment> paymentList;
    BillStatus billStatus;
}
