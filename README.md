# Smart Healthcare Appointment System
A modern Spring Boot 3 application designed to streamline hospital management, allowing patients, doctors, and administrators to efficiently handle appointments, prescriptions, and medical records. The system combines relational databases (MySQL/PostgreSQL) for structured data and MongoDB for flexible prescription and medical record storage developed as a part of my training at Exalt LTD.  

## Features

### Authentication & Authorization
- Secure login with **JWT**
- Paged Endpoints
- Role-based access:
  - **Admin:** Manage doctors and patients.  
  - **Doctor:** Handle appointments and prescriptions.  
  - **Patient:** Book or cancel appointments, view prescriptions and medical records.  

### Doctor Management
- Admins can add, update, register or remove doctors.  
- Patients can search doctors by **specialty**.  
- Doctor data is cached for fast access using Hibernate’s caching mechanisms.  

### Patient Management
- Admins can add, update, remove or register new patients.  
- Patients can update personal information.  

### Appointment & Slot System
- Each doctor defines their own **available slots**.  
- Patients can select a slot that fits their schedule.
- Patient Book/Cancel appointmens
- The system prevents **double-booking** by checking slot availability.  
- Doctors can mark appointments as completed.  
- Booking and cancellation actions are logged using **Spring AOP**.  

### Prescription & Medical Records
- Doctors can add prescriptions and notes for patients.  
- All prescriptions are stored in **MongoDB** for flexibility.  
- Patients can view their prescription history and lab reports.
- a medical record will be created for every patient and it will contain all of his medical info.

### Logging
- Uses **Spring AOP** to log key actions:  
  - Appointment bookings and cancellations.  
  - Prescription creation.  
- Detailed logs help track user actions and system behavior.  

### Testing
- JUnit and Mockito used to test:  
  - Appointment booking logic, including double-booking prevention.  
  - CRUD operations for doctors and patients.

## Architecture
- **Backend:** Spring Boot 3 with REST APIs.  
- **Database Layer:**  
  - Relational DB Postgres for structured data like doctors, patients, and appointments.  
  - MongoDB for unstructured data like prescriptions and medical records.  
- **Security:** Spring Security with role-based authorization with JWT.  
- **Caching:** Hibernate first- and second-level caching for frequently accessed entities (e.g., doctors).  
- **Logging:** Aspect-Oriented Programming (AOP) for tracking actions.  
- **Testing:** Unit and integration testing with JUnit + Mockito.

# How to run 

### Prerequisites
- Java 17  
- Spring Boot 3  
- Maven  
- PostgreSQL 
- MongoDB  

## Setup
1. Clone the repository:  
   ```bash
   git clone https://github.com/Kareem-izzat/SmartHealthcareAppointmentSystem.git
   cd SmartHealthcareAppointmentSystem
### Local Setup

1. **Update `application.properties` (or create a `.env`) with your database credentials**
2. **Build and run the application:**
 ```bash
  mvn clean package -DskipTests
  java -jar target/SmartHealthcareAppointmentSystem-0.0.1-SNAPSHOT.jar
  ```
### Docker Setup
### Prerequisites

- Docker
- Docker Compose
- Update you .env file with your database credentials 
### steps
1.**Build the backend Docker image**
```bash
  docker build -t smarthealthcareappointmentsystem-backend .
  ```
2.**Tag the image for Docker Hub**
```bash
  docker push <your_dockerhub_username>/smart-healthcare-backend:1.0
  ```
3..**Start all services using Docker Compose:**
```bash
  docker-compose up -d
  ```
4.**to Stop the services**
```bash
  docker-compose down 
  ```
