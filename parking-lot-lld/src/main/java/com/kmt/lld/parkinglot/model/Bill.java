package com.kmt.lld.parkinglot.model;

import com.kmt.lld.parkinglot.enums.BillStatus;
import java.util.List;

public class Bill {
    int id;
    Ticket ticket;
    double amount;
    List<Payment> paymentList;
    BillStatus billStatus;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public double getAmount() {
        return amount;
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public BillStatus getBillStatus() {
        return billStatus;
    }
}
