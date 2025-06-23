package com.femiproject.parceldeliverysystem;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Admin {
    private List<Customer> customers = new ArrayList<>();
    private List<Driver> drivers = new ArrayList<>();
    private List<Parcel> parcels = new ArrayList<>();
    private final ObjectMapper mapper;

    public Admin() {
        mapper = new ObjectMapper().registerModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        loadData();
    }

    private void loadData() {
        try {
            File c = new File("customers.json");
            File d = new File("drivers.json");
            File p = new File("parcels.json");

            if (c.exists())
                customers = mapper.readValue(c, new TypeReference<>() {
                });
            if (d.exists())
                drivers = mapper.readValue(d, new TypeReference<>() {
                });
            if (p.exists())
                parcels = mapper.readValue(p, new TypeReference<>() {
                });
        } catch (IOException e) {
            System.out.println("Error loading: " + e.getMessage());
        }
    }

    private void saveData() {
        try {
            mapper.writeValue(new File("customers.json"), customers);
            mapper.writeValue(new File("drivers.json"), drivers);
            mapper.writeValue(new File("parcels.json"), parcels);
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

    public void registerCustomer(Scanner sc) {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter phone: ");
        String phone = sc.nextLine();
        Customer customer = new Customer(name, phone);
        customers.add(customer);
        saveData();
        System.out.println("Registered customer ID: " + customer.getCustomerId());
    }

    public void createDriver(Scanner sc) {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter phone: ");
        String phone = sc.nextLine();
        Driver d = new Driver(name, phone);
        drivers.add(d);
        saveData();
        System.out.println("Registered driver ID: " + d.getDriverId());
    }

    public void createParcel(Scanner sc) {
        System.out.print("Enter customer ID: ");
        String cid = sc.nextLine();
        Customer customer = customers.stream()
                .filter(c -> c.getCustomerId().equals(cid))
                .findFirst()
                .orElse(null);
        if (customer == null) {
            return;
        }
        System.out.print("Weight (kg): ");
        double w = sc.nextDouble();
        sc.nextLine();
        System.out.print("Origin: ");
        String origin = sc.nextLine();
        System.out.print("Destination: ");
        String dest = sc.nextLine();
        double cost = 500 + (w * 100);
        Parcel percel = new Parcel(w, origin, dest, cost, cid);
        parcels.add(percel);
        customer.getParcels().add(percel.getParcelId());
        saveData();
        System.out.println("Parcel created with ID: " + percel.getParcelId());
    }

    public void assignParcelToDriver(Scanner sc) {
        System.out.print("Enter driver ID: ");
        String did = sc.nextLine();
        Driver d = drivers.stream()
                .filter(x -> x.getDriverId().equals(did))
                .findFirst()
                .orElse(null);
        if (d == null)
            return;
        for (Parcel p : parcels) {
            if (p.getDriverId() == null) {
                p.setDriverId(did);
                d.getAssignedParcelIds().add(p.getParcelId());
                System.out.println("Assigned parcel " + p.getParcelId() + " to driver.");
                break;
            }
        }
        saveData();
    }

    public void updateParcelStatus(Scanner sc) {
        System.out.print("Enter parcel ID: ");
        String pid = sc.nextLine();
        Parcel p = parcels.stream()
                .filter(x -> x.getParcelId().equals(pid))
                .findFirst()
                .orElse(null);
        if (p == null)
            return;
        p.setStatus(Status.DELIVERED);
        p.setDeliveryDate(LocalDateTime.now());
        saveData();
        System.out.println("Status updated.");
    }

    public void trackParcel(Scanner sc) {
        System.out.print("Enter parcel ID: ");
        String pid = sc.nextLine();
        Parcel p = parcels.stream()
                .filter(x -> x.getParcelId().equals(pid))
                .findFirst()
                .orElse(null);
        if (p != null)
            System.out.println("Status: " + p.getStatus());
        else
            System.out.println("Parcel not found.");
    }

    public void listParcelsByCustomer(Scanner sc) {
        System.out.print("Enter customer ID: ");
        String cid = sc.nextLine();
        parcels.stream()
                .filter(p -> cid.equals(p.getCustomerId()))
                .forEach(p -> System.out.println(p.getParcelId()));
    }

    public void listParcelsByDriver(Scanner sc) {
        System.out.print("Enter driver ID: ");
        String did = sc.nextLine();
        parcels.stream()
                .filter(p -> did.equals(p.getDriverId()))
                .forEach(p -> System.out.println(p.getParcelId()));
    }

}
