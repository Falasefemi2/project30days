package com.femiproject.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("john", "password123");
    }

    @Test
    void testAuthenticate() {
        assertTrue(user.authenticate("password123"));
    }

    @Test
    void testFailedAuthenticate() {
        assertFalse(user.authenticate("wrongpassword"));
    }

    @Test
    void testDeposit() {
        boolean result = user.deposit(100);
        assertTrue(result);
        assertEquals(100.0, user.getBalance());
        assertEquals(1, user.getTransactions().size());
        assertEquals(TransactionType.DEPOSIT, user.getTransactions().get(0).getType());
        assertEquals("john", user.getTransactions().get(0).getReceiverName());
    }

    @Test
    void testFailedDeposit() {
        assertFalse(user.deposit(0.0));
        assertFalse(user.deposit(-50.0));
        assertEquals(0.0, user.getBalance());
        assertTrue(user.getTransactions().isEmpty());
    }

    @Test
    void testReceiverMoney() {
        user.setBalance(100.0);
        user.receiverMoney("mary", 150.0);
        assertEquals(250.0, user.getBalance());
        assertEquals(1, user.getTransactions().size());

        Transaction transaction = user.getTransactions().get(0);
        assertEquals(TransactionType.TRANSFER_IN, transaction.getType());
        assertEquals("mary", transaction.getSenderName());
    }

    @Test
    void testTransfer() {
        user.deposit(150.0);
        boolean result = user.transfer("mary", 50.0);
        assertTrue(result);
        assertEquals(100.0, user.getBalance());
        assertEquals(2, user.getTransactions().size());

        Transaction transaction = user.getTransactions().get(1);
        assertEquals(TransactionType.TRANSFER_OUT, transaction.getType());
        assertEquals("john", transaction.getSenderName());
        assertEquals("mary", transaction.getReceiverName());
    }

    @Test
    void testFailedTransfer() {
        user.deposit(100.0);
        assertFalse(user.transfer(null, 50.0));
        assertFalse(user.transfer("", 600.0));
        assertFalse(user.transfer("john", 60.0));
        assertFalse(user.transfer("mary", 600.0));

        assertEquals(100.0, user.getBalance());
        assertEquals(1, user.getTransactions().size());

    }

    @Test
    void testWithdraw() {
        user.deposit(500.0);
        boolean result = user.withdraw(100.0);
        assertTrue(result);
        assertEquals(400.0, user.getBalance());
        assertEquals(2, user.getTransactions().size());
        assertEquals(TransactionType.WITHDRAW, user.getTransactions().get(1).getType());
    }

    @Test
    void testFailedWithdraw() {
        user.deposit(50.0);
        assertFalse(user.withdraw(500.0));
        assertFalse(user.withdraw(-100.0));
        assertFalse(user.withdraw(0.0));
        assertEquals(50.0, user.getBalance());
        assertEquals(1, user.getTransactions().size());
    }
}
