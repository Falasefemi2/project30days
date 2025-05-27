package com.femiproject.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

public class BankSystemTest {

    @Test
    void testAuthUser() throws InterruptedException {

        BankSystem bankSystem = new BankSystem();
        bankSystem.createUser("femi", "1234");

        Thread.sleep(1000);

        assertNotNull(bankSystem.authUser("femi", "123"));
        assertNull(bankSystem.authUser("femi", "wrong"), "Auth should fail with wrong password");
    }

    @Test
    void testCreateUser() throws InterruptedException {

        BankSystem bankSystem = new BankSystem();
        bankSystem.createUser("femi", "password123");
        Thread.sleep(1000);

        User user = bankSystem.authUser("femi", "password123");

        assertNotNull(user, "User should be created and authenticated");
        assertEquals("femi", user.getUsername());
    }

    @Test
    void testDeposit() throws InterruptedException {

        BankSystem bankSystem = new BankSystem();
        bankSystem.createUser("femi", "pass");

        bankSystem.deposit("femi", 100.0);

        Thread.sleep(1000);
        User user = bankSystem.authUser("femi", "pass");
        assertEquals(100.0, user.getBalance(), 0.01);
    }

    @Test
    void testDisplayTransactionHistory() throws InterruptedException {

        BankSystem bankSystem = new BankSystem();
        bankSystem.createUser("femi", "pass");
        bankSystem.deposit("femi", 100.0);
        bankSystem.withdraw("femi", 30.0);
        Thread.sleep(200);

        User user = bankSystem.findUser("femi");
        assertEquals(2, user.getTransactions().size());
    }

    @Test
    void testFindUser() throws InterruptedException {

        BankSystem bankSystem = new BankSystem();
        bankSystem.createUser("femi", "pass");
        Thread.sleep(1000);

        User user = bankSystem.findUser("femi");
        assertNotNull(user);
        assertEquals("femi", user.getUsername());

    }

    @Test
    void testTransfer() throws InterruptedException {

        BankSystem bankSystem = new BankSystem();
        bankSystem.createUser("femi", "pass");
        bankSystem.createUser("bola", "pass");

        bankSystem.deposit("femi", 100.0);
        Thread.sleep(100);

        bankSystem.transfer("femi", "bola", 40.0);
        Thread.sleep(200);

        assertEquals(60.0, bankSystem.authUser("femi", "pass").getBalance(), 0.01);
        assertEquals(40.0, bankSystem.authUser("bola", "pass").getBalance(), 0.01);
    }

    @Test
    void testViewBalance() throws InterruptedException {

        BankSystem bankSystem = new BankSystem();
        bankSystem.createUser("femi", "pass");
        bankSystem.deposit("femi", 150.0);
        Thread.sleep(100);

        User user = bankSystem.findUser("femi");
        assertEquals(150.0, user.getBalance(), 0.01);
    }

    @Test
    void testWithdraw() throws InterruptedException {
        BankSystem bankSystem = new BankSystem();
        bankSystem.createUser("femi", "pass");
        bankSystem.deposit("femi", 200.0);
        Thread.sleep(100);

        bankSystem.withdraw("femi", 50.0);
        Thread.sleep(100);

        User user = bankSystem.authUser("femi", "pass");
        assertEquals(150.0, user.getBalance(), 0.01);
    }
}
