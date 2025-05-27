package com.femiproject.bank;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {

    private String username;
    private String password;
    private double balance;
    private final List<Transaction> transactions;

    @JsonIgnore
    private final Object lock = new Object();

    public User() {
        this.transactions = new ArrayList<>();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }

    public void addTransactions(Transaction transaction) {
        transactions.add(transaction);
    }

    public boolean deposit(double amount) {
        synchronized (lock) {
            if (amount <= 0) {
                return false;
            }
            balance += amount;
            addTransactions(new Transaction(TransactionType.DEPOSIT, amount, this.username));
            return true;
        }
    }

    public boolean withdraw(double amount) {
        synchronized (lock) {
            if (amount <= 0 || amount > balance) {
                return false;
            }
            balance -= amount;
            addTransactions(new Transaction(TransactionType.WITHDRAW, amount, username, this.username));
            return true;
        }
    }

    public boolean transfer(String receiverName, double amount) {
        synchronized (lock) {
            if (amount <= 0 || amount > balance) {
                return false;
            }

            if (receiverName == null || receiverName.trim().isEmpty()) {
                return false;
            }

            if (receiverName.equals(this.username)) {
                return false;
            }
            balance -= amount;
            addTransactions(new Transaction(TransactionType.TRANSFER_OUT, amount, this.username, receiverName));
            return true;
        }
    }

    public void receiverMoney(String senderName, double amount) {
        synchronized (lock) {
            if (amount > 0 && senderName != null) {
                balance += amount;
            }
            addTransactions(new Transaction(TransactionType.TRANSFER_IN, amount, senderName, this.username));
        }
    }

    @JsonIgnore
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public String getUsername() {
        synchronized (lock) {
            return username;
        }
    }

    public String getPassword() {
        synchronized (lock) {
            return password;
        }
    }

    public double getBalance() {
        synchronized (lock) {
            return balance;
        }
    }

    public List<Transaction> getTransactions() {
        synchronized (lock) {
            return transactions;
        }
    }

    public Object getLock() {
        return lock;
    }

    @Override
    public String toString() {
        synchronized (lock) {
            return String.format("User[%s, Balance: %.2f]", username, balance);
        }
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
