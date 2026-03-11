# Miracle Hospital Management System

A hospital management system built with Java Spring Boot, supporting Doctor and Patient Android apps with an Admin web panel.

## Tech Stack

- **Backend:** Java Spring Boot
- **Database:** MySQL
- **ORM:** Spring Data JPA + Hibernate
- **Frontend (Admin):** JSP + Bootstrap 5
- **Authentication:** OTP based (WhatsApp/SMS)
- **Push Notifications:** Firebase FCM
- **Build Tool:** Maven

## Features

- Patient app API (login, appointments, chat, documents)
- Doctor app API (login, appointments, chat, notifications)
- Admin panel (manage doctors, patients, appointments, app config)
- WhatsApp & SMS OTP integration
- Firebase push notifications

## Requirements

- Java 17+
- MySQL 8.0+
- Maven 3.6+

## Setup

1. Clone the repository
   git clone https://github.com/vivartharepos/Backend_JAVA_Miracle_Hospital.git

2. Create a .env file in the project root
   DB_PASSWORD=your_db_password
   JWT_SECRET=your_jwt_secret
   GUPSHUP_API_KEY=your_gupshup_key
   CLOUD_WA_API_KEY=your_cloud_wa_key
   SMS_USER=your_sms_user
   SMS_API_KEY=your_sms_key
   FIREBASE_PROJECT_ID=your_firebase_project_id

3. Add firebase-credentials.json to src/main/resources/

4. Build and run
   mvn clean install -DskipTests
   mvn spring-boot:run

5. Access admin panel at http://localhost:8080/admin/login

## API Base URL

http://localhost:8080