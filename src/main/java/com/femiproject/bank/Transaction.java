package com.femiproject.bank;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    private TransactionType type;
    private double amount;
    private String senderName;
    private String receiverName;
    private LocalDateTime timeStamp;

    public Transaction() {
    }

    public Transaction(TransactionType type, double amount) {
        this.type = type;
        this.amount = amount;
        this.senderName = null;
        this.receiverName = null;
        this.timeStamp = LocalDateTime.now();
    }

    public Transaction(TransactionType type, double amount, String senderName, String receiverName) {
        this.type = type;
        this.amount = amount;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.timeStamp = LocalDateTime.now();
    }

    public Transaction(TransactionType type, double amount, String accountName) {
        this.type = type;
        this.amount = amount;
        if (type == TransactionType.DEPOSIT) {
            this.receiverName = accountName;
            this.senderName = null;
        } else if (type == TransactionType.WITHDRAW) {
            this.senderName = accountName;
            this.receiverName = null;
        }
        this.timeStamp = LocalDateTime.now();
    }

    @Override
    public String toString() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateStr = timeStamp.format(formatter);

        StringBuilder sb = new StringBuilder();
        sb.append("Transaction [type=").append(type)
                .append(", amount=").append(amount);

        if (senderName != null) {
            sb.append(", senderName=").append(senderName);
        }
        if (receiverName != null) {
            sb.append(", receiverName=").append(receiverName);
        }

        sb.append(", timeStamp=").append(dateStr).append("]");
        return sb.toString();
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

}
