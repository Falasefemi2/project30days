package com.femiproject.bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VirtualBankSimulation {

    private static final int NUM_USERS = 100;
    private static final int OPERATIONS_PER_USER = 5;
    private static final double MIN_DEPOSIT = 100.0;
    private static final double MAX_DEPOSIT = 5000.0;
    private static final double MIN_TRANSFER = 10.0;
    private static final double MAX_TRANSFER = 500.0;

    private final BankSystem bankSystem;
    private final Random random;
    private final List<String> usernames;
    private final AtomicInteger operationsCompleted;
    private final AtomicLong totalOperationTime;

    public VirtualBankSimulation() {
        this.bankSystem = new BankSystem();
        this.random = new Random();
        this.usernames = new ArrayList<>();
        this.operationsCompleted = new AtomicInteger(0);
        this.totalOperationTime = new AtomicLong(0);
    }

    public void runSimulation() {
        System.out.println("=== Virtual Bank Performance Simulation ===");
        System.out.println("Creating " + NUM_USERS + " users...");

        long startTime = System.currentTimeMillis();

        // Phase 1: Create users
        createUsers();

        // Wait for user creation to complete
        waitForUserCreation();

        long userCreationTime = System.currentTimeMillis() - startTime;
        System.out.println("User creation completed in: " + userCreationTime + "ms");
        System.out.println("Actual users created: " + bankSystem.getUsers().size());

        // Phase 2: Initial deposits
        System.out.println("\nPerforming initial deposits...");
        long depositStartTime = System.currentTimeMillis();
        performInitialDeposits();
        waitForOperations(NUM_USERS, "Initial deposits");
        long depositTime = System.currentTimeMillis() - depositStartTime;
        System.out.println("Initial deposits completed in: " + depositTime + "ms");

        // Phase 3: Concurrent operations simulation
        System.out.println("\nStarting concurrent operations simulation...");
        long simulationStartTime = System.currentTimeMillis();
        performConcurrentOperations();

        long simulationTime = System.currentTimeMillis() - simulationStartTime;
        long totalTime = System.currentTimeMillis() - startTime;

        // Display results
        displayResults(userCreationTime, depositTime, simulationTime, totalTime);

        // Cleanup
        bankSystem.shutDown();
    }

    private void createUsers() {
        CountDownLatch userCreationLatch = new CountDownLatch(NUM_USERS);

        for (int i = 1; i <= NUM_USERS; i++) {
            final String username = "user" + i;
            final String fullName = "User " + i;
            final String password = "password" + i;

            usernames.add(username);

            CompletableFuture.runAsync(() -> {
                try {
                    bankSystem.createUser(username, password);
                } finally {
                    userCreationLatch.countDown();
                }
            });
        }

        try {
            userCreationLatch.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("User creation interrupted");
        }
    }

    private void waitForUserCreation() {
        // Give some time for all user creation operations to complete
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void performInitialDeposits() {
        operationsCompleted.set(0);

        for (String username : usernames) {
            double initialDeposit = MIN_DEPOSIT + (random.nextDouble() * (MAX_DEPOSIT - MIN_DEPOSIT));

            CompletableFuture.runAsync(() -> {
                long opStart = System.nanoTime();
                bankSystem.deposit(username, initialDeposit);
                long opTime = System.nanoTime() - opStart;
                totalOperationTime.addAndGet(opTime);
                operationsCompleted.incrementAndGet();
            });
        }
    }

    private void performConcurrentOperations() {
        operationsCompleted.set(0);
        int totalOperations = NUM_USERS * OPERATIONS_PER_USER;

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        // Create shuffled list of usernames for random operations
        List<String> shuffledUsers = new ArrayList<>(usernames);

        for (int i = 0; i < totalOperations; i++) {
            Collections.shuffle(shuffledUsers);
            final String currentUser = shuffledUsers.get(random.nextInt(shuffledUsers.size()));

            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                performRandomOperation(currentUser, shuffledUsers);
            }, executor);

            futures.add(future);
        }

        // Wait for all operations to complete
        CompletableFuture<Void> allOperations = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        try {
            allOperations.get(60, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.err.println("Some operations failed or timed out: " + e.getMessage());
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void performRandomOperation(String username, List<String> allUsers) {
        long opStart = System.nanoTime();

        try {
            int operationType = random.nextInt(100);

            if (operationType < 40) {
                // 40% chance - Deposit
                double amount = MIN_DEPOSIT + (random.nextDouble() * (MAX_DEPOSIT - MIN_DEPOSIT));
                bankSystem.deposit(username, amount);

            } else if (operationType < 60) {
                // 20% chance - Withdrawal
                double amount = MIN_TRANSFER + (random.nextDouble() * (MAX_TRANSFER - MIN_TRANSFER));
                bankSystem.withdraw(username, amount);

            } else {
                // 40% chance - Transfer
                String targetUser;
                do {
                    targetUser = allUsers.get(random.nextInt(allUsers.size()));
                } while (targetUser.equals(username));

                double amount = MIN_TRANSFER + (random.nextDouble() * (MAX_TRANSFER - MIN_TRANSFER));
                bankSystem.transfer(username, targetUser, amount);
            }
        } finally {
            long opTime = System.nanoTime() - opStart;
            totalOperationTime.addAndGet(opTime);
            operationsCompleted.incrementAndGet();
        }
    }

    private void waitForOperations(int expectedOps, String operationType) {
        int maxWaitSeconds = 30;
        int waitedSeconds = 0;

        while (operationsCompleted.get() < expectedOps && waitedSeconds < maxWaitSeconds) {
            try {
                Thread.sleep(1000);
                waitedSeconds++;
                if (waitedSeconds % 5 == 0) {
                    System.out.println(operationType + " progress: "
                            + operationsCompleted.get() + "/" + expectedOps);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void displayResults(long userCreationTime, long depositTime,
            long simulationTime, long totalTime) {
        System.out.println("\n=== SIMULATION RESULTS ===");
        System.out.println("Total Users: " + bankSystem.getUsers().size());
        System.out.println("Total Operations Completed: " + operationsCompleted.get());
        System.out.println();

        System.out.println("Performance Metrics:");
        System.out.println("- User Creation Time: " + userCreationTime + "ms");
        System.out.println("- Initial Deposits Time: " + depositTime + "ms");
        System.out.println("- Concurrent Operations Time: " + simulationTime + "ms");
        System.out.println("- Total Simulation Time: " + totalTime + "ms");
        System.out.println();

        if (totalOperationTime.get() > 0) {
            double avgOperationTimeMs = totalOperationTime.get() / 1_000_000.0 / operationsCompleted.get();
            System.out.println("Average Operation Time: " + String.format("%.2f", avgOperationTimeMs) + "ms");
        }

        int totalExpectedOperations = NUM_USERS + (NUM_USERS * OPERATIONS_PER_USER);
        double operationsPerSecond = (double) operationsCompleted.get() / (totalTime / 1000.0);
        System.out.println("Operations per Second: " + String.format("%.2f", operationsPerSecond));

// Display sample user balances
        System.out.println("\nSample User Balances (first 10 users):");
        List<User> users = bankSystem.getUsers();
        for (int i = 0; i < Math.min(10, users.size()); i++) {
            User user = users.get(i);
            System.out.printf("- %s: $%.2f%n", user.getUsername(), user.getBalance());
        }

// Calculate total money in system
        double totalBalance = users.stream()
                .mapToDouble(User::getBalance)
                .sum();
        System.out.println();
        System.out.println("Total Money in System: $" + String.format("%.2f", totalBalance));

// Memory usage
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory Usage: " + (usedMemory / 1024 / 1024) + " MB");
    }

    public static void main(String[] args) {
        System.out.println("Starting Virtual Banking System Simulation...");
        System.out.println("This will create " + NUM_USERS + " users and perform concurrent operations");
        System.out.println("to test virtual thread performance.\n");

        VirtualBankSimulation simulation = new VirtualBankSimulation();
        simulation.runSimulation();

        System.out.println("\nSimulation completed!");
    }
}
