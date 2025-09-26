🏥 Hospital Management System

A Java Swing + MySQL based desktop application that helps hospitals manage patients, staff, appointments, prescriptions, and doctors efficiently.

✨ Features
🔐 Authentication

Login with username (from full name), password, and role (Staff/Patient).

Role-based dashboards.

Logout functionality.

👩‍⚕️ Staff Dashboard

Add new patients.

Schedule appointments.

Manage prescriptions.

Search patient records.

Generate reports (Appointments, Prescriptions, Patients).

🧑‍🤝‍🧑 Patient Dashboard

View appointments.

View prescriptions.

Search doctors by specialty.

🩺 Doctor Management

Store and search doctors with:

Name, specialty, phone, email, license number, availability, working hours.

🗄 Database Schema
Tables

users → id, username, password, role

patients → id, full_name, dob, gender, phone, email, address, photo_path

doctors → id, full_name, specialty, phone, email, license_no, available, working_hours

appointments → id, patient_id, doctor_id, appointment_date, time_slot, status, notes

prescriptions → id, appointment_id, prescribed_by, medicine, dosage, instructions

Relationships

A patient ➝ multiple appointments.

Appointment ➝ linked to one doctor, can have multiple prescriptions.

⚙️ Technology Stack

Language: Java

GUI: Swing (JFrame, JPanel, GridLayout, etc.)

Database: MySQL (JDBC connection)

IDE: IntelliJ IDEA / Eclipse / NetBeans


🔒 Security Notes

Currently, passwords are stored as plain text (⚠️).

For production, use hashing (e.g., BCrypt, SHA-256).

🛠 Future Enhancements

Add Admin role for system-wide management.

Implement billing & payments module.

Email/SMS appointment notifications.

Upgrade UI to JavaFX or Web-based frontend.