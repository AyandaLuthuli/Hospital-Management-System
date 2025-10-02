package view;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class AddPatientForm extends JFrame {
    private JTextField fullNameField;
    private JTextField dobField;
    private JComboBox<String> genderBox;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField addressField;

    public AddPatientForm() {
        setTitle("Add New Patient");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Make the window non-resizable
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(10, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Full Name
        panel.add(new JLabel("Full Name:*"));
        fullNameField = new JTextField();
        panel.add(fullNameField);

        // DOB (yyyy-mm-dd)
        panel.add(new JLabel("Date of Birth:*"));
        dobField = new JTextField();
        panel.add(dobField);
        panel.add(new JLabel("(YYYY-MM-DD)"));
        panel.add(new JLabel(""));

        // Gender
        panel.add(new JLabel("Gender:*"));
        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        panel.add(genderBox);

        // Phone
        panel.add(new JLabel("Contact Number:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        // Email
        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        // Address
        panel.add(new JLabel("Address:"));
        addressField = new JTextField();
        panel.add(addressField);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save");
        JButton clearButton = new JButton("Clear");
        JButton backButton = new JButton("Back");

        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        panel.add(new JLabel("")); // Empty cell
        panel.add(buttonPanel);

        add(panel);

        // Button actions
        saveButton.addActionListener(e -> savePatient());
        clearButton.addActionListener(e -> clearForm());
        backButton.addActionListener(e -> goBackToDashboard());
    }

    private void goBackToDashboard() {
        // Close current form
        dispose();

        // Open staff dashboard
        SwingUtilities.invokeLater(() -> {
            // Assuming you have a StaffDashboard class
            // If the class name is different, adjust accordingly
            try {
                Class<?> dashboardClass = Class.forName("view.StaffDashboard");
                JFrame dashboard = (JFrame) dashboardClass.getDeclaredConstructor().newInstance();
                dashboard.setVisible(true);
            } catch (ClassNotFoundException e) {
                // If StaffDashboard doesn't exist in the view package, try other common names
                try {
                    Class<?> dashboardClass = Class.forName("view.StaffDashboardForm");
                    JFrame dashboard = (JFrame) dashboardClass.getDeclaredConstructor().newInstance();
                    dashboard.setVisible(true);
                } catch (ClassNotFoundException ex) {
                    // If no dashboard class found, show message and close
                    JOptionPane.showMessageDialog(this,
                            "Staff dashboard not found. Please contact administrator.",
                            "Navigation Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Error opening dashboard: " + ex.getMessage(),
                            "Navigation Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error opening dashboard: " + e.getMessage(),
                        "Navigation Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Alternative simpler implementation if you know the exact class name:
    private void goBackToDashboardSimple() {
        dispose(); // Close current form
        new StaffDashboard().setVisible(true); // Replace with your actual dashboard class
    }

    // Alternative implementation with confirmation:
    private void goBackToDashboardWithConfirmation() {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to go back? Any unsaved data will be lost.",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            dispose();
            // Open staff dashboard - adjust class name as needed
            SwingUtilities.invokeLater(() -> {
                new StaffDashboard().setVisible(true);
            });
        }
    }

    private void savePatient() {
        if (!validateInputs()) {
            return;
        }

        String fullName = fullNameField.getText().trim();
        String dob = dobField.getText().trim();
        String gender = genderBox.getSelectedItem().toString();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        try (Connection conn = ConnectionBD.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            try {
                // Insert into patients
                String patientSql = "INSERT INTO patients (full_name, dob, gender, phone, email, address) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(patientSql)) {
                    stmt.setString(1, fullName);
                    stmt.setString(2, dob);
                    stmt.setString(3, gender);
                    stmt.setString(4, phone.isEmpty() ? null : phone);
                    stmt.setString(5, email.isEmpty() ? null : email);
                    stmt.setString(6, address.isEmpty() ? null : address);
                    stmt.executeUpdate();
                }

                // Insert into users (username = full name, password = 123456)
                String defaultPassword = "123456";
                String userSql = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, 'Patient')";
                try (PreparedStatement stmt2 = conn.prepareStatement(userSql)) {
                    stmt2.setString(1, fullName); // username = full name
                    stmt2.setString(2, defaultPassword);
                    stmt2.executeUpdate();
                }

                conn.commit(); // Commit both
                JOptionPane.showMessageDialog(this, "Patient and user account created successfully!\nDefault password: 123456");
                dispose();

            } catch (SQLException ex) {
                conn.rollback(); // Rollback if error
                // Check for duplicate entry
                if (ex.getMessage().contains("Duplicate entry")) {
                    JOptionPane.showMessageDialog(this, "Error: A patient with this name already exists.");
                } else {
                    throw ex;
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private boolean validateInputs() {
        // Full Name validation
        String fullName = fullNameField.getText().trim();
        if (fullName.isEmpty()) {
            showError("Full Name is required.");
            fullNameField.requestFocus();
            return false;
        }
        if (fullName.length() < 2 || fullName.length() > 100) {
            showError("Full Name must be between 2 and 100 characters.");
            fullNameField.requestFocus();
            return false;
        }
        if (!Pattern.matches("^[a-zA-Z\\s.'-]+$", fullName)) {
            showError("Full Name can only contain letters, spaces, apostrophes, hyphens, and periods.");
            fullNameField.requestFocus();
            return false;
        }

        // Date of Birth validation
        String dob = dobField.getText().trim();
        if (dob.isEmpty()) {
            showError("Date of Birth is required.");
            dobField.requestFocus();
            return false;
        }
        if (!isValidDate(dob)) {
            showError("Please enter a valid date in YYYY-MM-DD format.");
            dobField.requestFocus();
            return false;
        }
        if (!isValidBirthDate(dob)) {
            showError("Date of Birth must be in the past and patient must be at least 1 year old.");
            dobField.requestFocus();
            return false;
        }

        // Phone validation (if provided)
        String phone = phoneField.getText().trim();
        if (!phone.isEmpty() && !isValidPhone(phone)) {
            showError("Please enter a valid phone number (10-15 digits, may include +, -, spaces, or parentheses).");
            phoneField.requestFocus();
            return false;
        }

        // Email validation (if provided)
        String email = emailField.getText().trim();
        if (!email.isEmpty() && !isValidEmail(email)) {
            showError("Please enter a valid email address.");
            emailField.requestFocus();
            return false;
        }

        // Address validation (if provided)
        String address = addressField.getText().trim();
        if (!address.isEmpty() && address.length() > 255) {
            showError("Address must not exceed 255 characters.");
            addressField.requestFocus();
            return false;
        }

        return true;
    }

    private boolean isValidDate(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isValidBirthDate(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthDate = LocalDate.parse(dateStr, formatter);
            LocalDate today = LocalDate.now();
            LocalDate minBirthDate = today.minusYears(150); // Reasonable maximum age
            LocalDate maxBirthDate = today.minusYears(1);   // Minimum 1 year old

            return birthDate.isAfter(minBirthDate) && birthDate.isBefore(maxBirthDate);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean isValidPhone(String phone) {
        // Allows international format: +1 (123) 456-7890 or local: 123-456-7890
        String phoneRegex = "^[\\+]?[\\d\\s\\-\\(\\)]{10,15}$";
        // Remove non-digit characters and check length
        String digitsOnly = phone.replaceAll("\\D", "");
        return Pattern.matches(phoneRegex, phone) && digitsOnly.length() >= 10 && digitsOnly.length() <= 15;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email);
    }

    private void clearForm() {
        fullNameField.setText("");
        dobField.setText("");
        genderBox.setSelectedIndex(0);
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
        fullNameField.requestFocus();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddPatientForm().setVisible(true));
    }
}