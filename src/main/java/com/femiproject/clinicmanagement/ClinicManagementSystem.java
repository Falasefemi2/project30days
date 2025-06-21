package com.femiproject.clinicmanagement;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClinicManagementSystem {
    private AppointmentManager appointmentManager;
    private DoctorManager doctorManager;
    private PatientManager patientManager;
    private Scanner scanner;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ClinicManagementSystem() {
        this.doctorManager = new DoctorManager();
        this.patientManager = new PatientManager();
        this.appointmentManager = new AppointmentManager(patientManager, doctorManager);
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the Clinic Management System!");

        while (true) {
            printMenu();
            int choice = getIntInput("Choose an option: ");

            switch (choice) {
                case 1 -> addDoctor();
                case 2 -> addPatient();
                case 3 -> bookAppointment();
                case 4 -> viewPatientAppointments();
                case 5 -> viewDoctorAppointments();
                case 6 -> listAllDoctors();
                case 7 -> listAllPatients();
                case 8 -> cancelAppointment();
                case 9 -> {
                    System.out.println("Thank you for using the Clinic Management System. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== Clinic Management System ===");
        System.out.println("1. Add Doctor");
        System.out.println("2. Add Patient");
        System.out.println("3. Book Appointment");
        System.out.println("4. View Patient Appointments");
        System.out.println("5. View Doctor Appointments");
        System.out.println("6. List All Doctors");
        System.out.println("7. List All Patients");
        System.out.println("8. Cancel Appointment");
        System.out.println("9. Exit");
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void addDoctor() {
        System.out.print("Enter doctor name: ");
        String name = scanner.nextLine();

        System.out.print("Enter specialization: ");
        String specialization = scanner.nextLine();

        Doctor doctor = new Doctor(name, specialization);
        doctorManager.addDoctor(doctor);
    }

    private void addPatient() {
        System.out.print("Enter patient name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Patient name cannot be empty.");
            return;
        }

        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        if (email.isEmpty()) {
            System.out.println("Email cannot be empty.");
            return;
        }

        System.out.print("Enter phone: ");
        String phone = scanner.nextLine().trim();

        if (phone.isEmpty()) {
            System.out.println("Phone cannot be empty.");
            return;
        }

        Patient patient = new Patient(name, email, phone);
        patientManager.addPatient(patient);
    }

    private void bookAppointment() {
        List<Doctor> doctors = doctorManager.listAllDoctor();
        if (doctors.isEmpty()) {
            System.out.println("No doctors available. Please add a doctor first.");
            return;
        }

        List<Patient> patients = patientManager.listAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients available. Please add a patient first.");
            return;
        }

        // Select doctor
        System.out.println("\nAvailable doctors:");
        for (int i = 0; i < doctors.size(); i++) {
            Doctor doctor = doctors.get(i);
            System.out.println((i + 1) + ". " + doctor.getName() + " (" + doctor.getSpecialization() + ")");
        }

        int doctorChoice = getIntInput("Select doctor (enter number): ") - 1;
        if (doctorChoice < 0 || doctorChoice >= doctors.size()) {
            System.out.println("Invalid doctor selection.");
            return;
        }
        Doctor selectedDoctor = doctors.get(doctorChoice);

        // Select patient
        System.out.println("\nAvailable patients:");
        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            System.out.println((i + 1) + ". " + patient.getName() + " (" + patient.getEmail() + ")");
        }

        int patientChoice = getIntInput("Select patient (enter number): ") - 1;
        if (patientChoice < 0 || patientChoice >= patients.size()) {
            System.out.println("Invalid patient selection.");
            return;
        }
        Patient selectedPatient = patients.get(patientChoice);

        // Show available slots
        List<LocalDateTime> availableSlots = selectedDoctor.getAvailableSlots();
        if (availableSlots.isEmpty()) {
            System.out.println("No available slots for " + selectedDoctor.getName());
            return;
        }

        System.out.println("\nAvailable slots for " + selectedDoctor.getName() + ":");
        for (int i = 0; i < Math.min(availableSlots.size(), 20); i++) { // Show max 20 slots
            LocalDateTime slot = availableSlots.get(i);
            System.out.println((i + 1) + ". " + slot.format(formatter));
        }

        int slotChoice = getIntInput("Select slot (enter number): ") - 1;
        if (slotChoice < 0 || slotChoice >= availableSlots.size()) {
            System.out.println("Invalid slot selection.");
            return;
        }

        LocalDateTime selectedSlot = availableSlots.get(slotChoice);
        appointmentManager.createAppointment(selectedPatient, selectedDoctor, selectedSlot);
    }

    private void viewPatientAppointments() {
        List<Patient> patients = patientManager.listAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients available.");
            return;
        }

        System.out.println("Select patient to view appointments:");
        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            System.out.println((i + 1) + ". " + patient.getName());
        }

        int choice = getIntInput("Enter patient number: ") - 1;
        if (choice < 0 || choice >= patients.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Patient selectedPatient = patients.get(choice);
        List<Appointment> appointments = appointmentManager.getAppointmentsByPatient(selectedPatient.getPatientId());

        System.out.println("\nAppointments for " + selectedPatient.getName() + ":");
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            for (Appointment appointment : appointments) {
                Doctor doctor = doctorManager.findDoctorById(appointment.getDoctorId());
                String doctorName = doctor != null ? doctor.getName() : "Unknown Doctor";
                System.out.println("- " + appointment.getAppointmentTime().format(formatter) +
                        " with Dr. " + doctorName + " (Status: " + appointment.getStatus() + ")");
            }
        }
    }

    private void viewDoctorAppointments() {
        List<Doctor> doctors = doctorManager.listAllDoctor();
        if (doctors.isEmpty()) {
            System.out.println("No doctors available.");
            return;
        }

        System.out.println("Select doctor to view appointments:");
        for (int i = 0; i < doctors.size(); i++) {
            Doctor doctor = doctors.get(i);
            System.out.println((i + 1) + ". " + doctor.getName());
        }

        int choice = getIntInput("Enter doctor number: ") - 1;
        if (choice < 0 || choice >= doctors.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Doctor selectedDoctor = doctors.get(choice);
        List<Appointment> appointments = appointmentManager.getAppointmentsByDoctor(selectedDoctor.getDoctorId());

        System.out.println("\nAppointments for Dr. " + selectedDoctor.getName() + ":");
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            for (Appointment appointment : appointments) {
                Patient patient = patientManager.findPatientById(appointment.getPatientId());
                String patientName = patient != null ? patient.getName() : "Unknown Patient";
                System.out.println("- " + appointment.getAppointmentTime().format(formatter) +
                        " with " + patientName + " (Status: " + appointment.getStatus() + ")");
            }
        }
    }

    private void listAllDoctors() {
        List<Doctor> doctors = doctorManager.listAllDoctor();
        if (doctors.isEmpty()) {
            System.out.println("No doctors available.");
        } else {
            System.out.println("\nAll doctors:");
            for (Doctor doctor : doctors) {
                int availableSlots = doctor.getAvailableSlots().size();
                System.out.println("- " + doctor.getName() + " (" + doctor.getSpecialization() +
                        ") - " + availableSlots + " available slots");
            }
        }
    }

    private void listAllPatients() {
        List<Patient> patients = patientManager.listAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients available.");
        } else {
            System.out.println("\nAll patients:");
            for (Patient patient : patients) {
                int appointmentCount = patient.getAppointments().size();
                System.out.println("- " + patient.getName() + " (" + patient.getEmail() +
                        ") - " + appointmentCount + " appointments");
            }
        }
    }

    private void cancelAppointment() {
        List<Appointment> allAppointments = appointmentManager.getAllAppointments();
        List<Appointment> scheduledAppointments = allAppointments.stream()
                .filter(a -> a.getStatus() == Status.SCHEDULED)
                .toList();

        if (scheduledAppointments.isEmpty()) {
            System.out.println("No scheduled appointments available to cancel.");
            return;
        }

        System.out.println("\nScheduled appointments:");
        for (int i = 0; i < scheduledAppointments.size(); i++) {
            Appointment appointment = scheduledAppointments.get(i);
            Patient patient = patientManager.findPatientById(appointment.getPatientId());
            Doctor doctor = doctorManager.findDoctorById(appointment.getDoctorId());

            String patientName = patient != null ? patient.getName() : "Unknown Patient";
            String doctorName = doctor != null ? doctor.getName() : "Unknown Doctor";

            System.out.println((i + 1) + ". " + appointment.getAppointmentTime().format(formatter) +
                    " - " + patientName + " with Dr. " + doctorName +
                    " (ID: " + appointment.getAppointmentId().substring(0, 8) + "...)");
        }

        int choice = getIntInput("Select appointment to cancel (enter number): ") - 1;
        if (choice < 0 || choice >= scheduledAppointments.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Appointment selectedAppointment = scheduledAppointments.get(choice);
        appointmentManager.cancelAppointment(selectedAppointment.getAppointmentId());
    }

    public static void main(String[] args) {
        ClinicManagementSystem system = new ClinicManagementSystem();
        system.start();
    }
}