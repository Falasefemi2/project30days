package com.femiproject.bank;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class BankSystem {

    private List<User> users;
    private ObjectMapper objectMapper;
    private final String DATA_FILE = "bank.json";

    @JsonIgnore
    private final Object lock = new Object();
    private final ExecutorService service;

    public BankSystem() {
        this.users = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.service = Executors.newVirtualThreadPerTaskExecutor();
        loadUsers();
    }

    private void loadUsers() {
        try {
            synchronized (lock) {
                File file = new File(DATA_FILE);
                if (file.exists() && file.length() > 0) {
                    List<User> loadedUsers = objectMapper.readValue(file, new TypeReference<List<User>>() {
                    });
                    if (loadedUsers != null) {
                        users.clear();
                        users.addAll(loadedUsers);
                        System.out.println("Loaded " + users.size() + " users from " + DATA_FILE);
                    } else {
                        System.out.println("Loaded data was null.");
                    }
                } else {
                    System.out.println("No existing user data found. Starting with empty user list.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users from " + DATA_FILE + ": " + e.getMessage());
        }
    }

    public void shutDown() {
        synchronized (lock) {
            saveUsers();
        }

        service.shutdown();
        try {
            if (!service.awaitTermination(5, TimeUnit.SECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            service.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void saveUsers() {
        synchronized (lock) {
            try {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), users);
            } catch (IOException e) {
                System.out.println("Error saving users: " + e.getMessage());
            }
        }
    }

    public List<User> getUsers() {
        synchronized (lock) {
            return new ArrayList<>(users);
        }
    }

    public User findUser(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public Future<Void> createUser(String username, String password) {
        return service.submit(() -> {
            synchronized (lock) {
                if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
                    System.out.println("Account creation failed: Invalid input");
                    return null;
                }
                if (findUser(username) != null) {
                    System.out.println("Account creation failed: Username already exists");
                    return null;
                }
                User newUser = new User(username, password);
                users.add(newUser);
                saveUsers();
                System.out.println("Account created successfully for: " + username);
                return null;
            }
        });
    }

    public User authUser(String username, String password) {
        synchronized (lock) {
            User user = findUser(username);
            if (user != null && user.authenticate(password)) {
                return user;
            }
            return null;
        }
    }

    public void deposit(String username, double amount) {
        service.submit(() -> {
            synchronized (lock) {
                User user = findUser(username);
                if (user != null && user.deposit(amount)) {
                    saveUsers();
                    System.out.printf("Deposited $%.2f successfully to %s's account%n", amount, username);
                } else {
                    System.out.println("Deposit failed: "
                            + (user == null ? "User not found" : "Invalid amount"));
                }
            }
        });
    }

    public void withdraw(String username, double amount) {
        service.submit(() -> {
            synchronized (lock) {
                User user = findUser(username);
                if (user != null && user.withdraw(amount)) {
                    saveUsers();
                    System.out.printf("Withdrew $%.2f successfully from %s's account%n", amount, username);
                } else {
                    System.out.println("Withdrawal failed: "
                            + (user == null ? "User not found" : "Insufficient funds or invalid amount"));
                }
            }
        });
    }

    public void transfer(String sendername, String receivername, double amount) {
        service.submit(() -> {
            synchronized (lock) {
                User sender = findUser(sendername);
                User receiver = findUser(receivername);

                if (sender == null) {
                    System.out.println("Transfer failed: Sender not found");
                    return;
                }

                if (receiver == null) {
                    System.out.println("Transfer failed: Receiver not found");
                    return;
                }

                if (amount <= 0) {
                    System.out.println("Transfer failed: Invalid amount");
                    return;
                }

                Object firstLock = sendername.compareTo(receivername) < 0 ? sender.getLock() : receiver.getLock();
                Object secondLock = sendername.compareTo(receivername) < 0 ? receiver.getLock() : sender.getLock();

                synchronized (firstLock) {
                    synchronized (secondLock) {
                        if (sender.transfer(receivername, amount)) {
                            receiver.receiverMoney(sendername, amount);
                            saveUsers();
                            System.out.printf("Transfer successful: $%.2f from %s to %s%n",
                                    amount, sendername, receivername);
                        } else {
                            System.out.println("Transfer failed: Insufficient funds");
                        }
                    }
                }
            }
        });
    }

    public void viewBalance(String username) {
        synchronized (lock) {
            User user = findUser(username);
            if (user != null) {
                System.out.printf("%s's balance: $%.2f%n", username, user.getBalance());
            } else {
                System.out.println("User not found: " + username);
            }
        }
    }

    public void displayTransactionHistory(String username) {
        synchronized (lock) {
            User user = findUser(username);
            if (user != null) {
                List<Transaction> transactions = user.getTransactions();
                System.out.println("Transaction History for " + username + ":");
                if (transactions.isEmpty()) {
                    System.out.println("No Transaction found.");
                } else {
                    transactions.forEach(System.out::println);
                }
            } else {
                System.out.println("User not found: " + username);
            }
        }
    }
}
