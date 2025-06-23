package com.femiproject.parceldeliverysystem;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Admin admin = new Admin();
        try (Scanner sc = new Scanner(System.in)) {
            int choice;
            do {
                System.out.println("\n=== Parcel Delivery System ===");
                System.out.println(
                        "1. Register Customer\n2. Register Driver\n3. Create Parcel\n4. Assign Parcel to Driver\n5. Update Parcel Status\n6. Track Parcel\n7. List Parcels by Customer\n8. List Parcels by Driver\n9. Exit");
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1 -> admin.registerCustomer(sc);
                    case 2 -> admin.createDriver(sc);
                    case 3 -> admin.createParcel(sc);
                    case 4 -> admin.assignParcelToDriver(sc);
                    case 5 -> admin.updateParcelStatus(sc);
                    case 6 -> admin.trackParcel(sc);
                    case 7 -> admin.listParcelsByCustomer(sc);
                    case 8 -> admin.listParcelsByDriver(sc);
                    case 9 -> System.out.println("Goodbye!");
                    default -> System.out.println("Invalid.");
                }
            } while (choice != 9);
        }
    }

}
