# Clinic Management System

A comprehensive Java-based clinic management system that handles doctors, patients, and appointments with a command-line interface.

## Features

### Core Functionality

- **Doctor Management**: Add and manage doctors with specializations
- **Patient Management**: Register and manage patient information
- **Appointment Scheduling**: Book, view, and cancel appointments
- **Availability Tracking**: Automatic slot generation and availability checking
- **Status Management**: Track appointment status (Scheduled, Completed, Canceled)

### Key Components

#### Doctor Management

- Add new doctors with name and specialization
- Automatic generation of available time slots (next 7 days, 9 AM - 5 PM)
- View all registered doctors
- Track doctor appointments

#### Patient Management

- Register new patients with name, email, and phone
- Input validation for required fields
- View all registered patients
- Track patient appointment history

#### Appointment System

- Book appointments between patients and doctors
- Select from available time slots
- View appointments by patient or doctor
- Cancel existing appointments
- Automatic slot management (removes booked slots, adds canceled slots back)

## Project Structure

```
clinicmanagement/
├── ClinicManagementSystem.java    # Main application class with CLI
├── Doctor.java                    # Doctor entity with availability management
├── Patient.java                   # Patient entity with contact information
├── Appointment.java               # Appointment entity with status tracking
├── Status.java                    # Enum for appointment status
├── DoctorManager.java             # Business logic for doctor operations
├── PatientManager.java            # Business logic for patient operations
├── AppointmentManager.java        # Business logic for appointment operations
└── README.md                      # This file
```

## Usage

### Running the Application

```bash
# Compile the project
javac -cp ".:path/to/jackson/*" *.java

# Run the main application
java com.femiproject.clinicmanagement.ClinicManagementSystem
```

### Main Menu Options

1. **Add Doctor** - Register a new doctor with specialization
2. **Add Patient** - Register a new patient with contact details
3. **Book Appointment** - Schedule an appointment between patient and doctor
4. **View Patient Appointments** - Display all appointments for a specific patient
5. **View Doctor Appointments** - Display all appointments for a specific doctor
6. **List All Doctors** - Show all registered doctors
7. **List All Patients** - Show all registered patients
8. **Cancel Appointment** - Cancel an existing appointment
9. **Exit** - Close the application

### Workflow Example

1. Add a doctor (e.g., "Dr. Smith" - "Cardiology")
2. Add a patient (e.g., "John Doe" - "john@email.com" - "555-0123")
3. Book an appointment by selecting the doctor, patient, and available time slot
4. View appointments to confirm booking
5. Cancel appointments if needed

## Technical Details

### Data Persistence

- Uses Jackson library for JSON serialization/deserialization
- Supports data persistence across application sessions
- Automatic UUID generation for entities

### Time Management

- Uses Java 8+ `LocalDateTime` for precise time handling
- Automatic slot generation for the next 7 days
- Business hours: 9 AM to 5 PM (8 slots per day)
- Real-time availability checking

### Validation

- Input validation for required fields
- Duplicate prevention for available slots
- Bounds checking for menu selections
- Error handling for invalid inputs

### Dependencies

- **Jackson**: JSON serialization/deserialization
- **Java 8+**: Time API and other modern features
- **UUID**: Unique identifier generation

## Entity Models

### Doctor

- `doctorId`: Unique identifier
- `name`: Doctor's full name
- `specialization`: Medical specialization
- `availableSlots`: List of available appointment times
- `appointments`: List of scheduled appointments

### Patient

- `patientId`: Unique identifier
- `name`: Patient's full name
- `email`: Contact email address
- `phone`: Contact phone number
- `appointments`: List of scheduled appointments

### Appointment

- `appointmentId`: Unique identifier
- `patientId`: Reference to patient
- `doctorId`: Reference to doctor
- `appointmentTime`: Scheduled date and time
- `status`: Current status (SCHEDULED, COMPLETED, CANCELED)

## Error Handling

- Graceful handling of invalid user inputs
- Validation of required fields
- Bounds checking for menu selections
- Informative error messages

## Contributing

This project is part of a 30-day coding challenge. Feel free to extend functionality or improve the existing codebase.
