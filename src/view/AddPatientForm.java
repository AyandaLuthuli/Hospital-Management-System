package view;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddPatientForm extends JFrame {
    private JTextField fullNameField;
    private JTextField dobField;
    private JComboBox<String> genderBox;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField addressField;

    public AddPatientForm() {
        setTitle("Add New Patient");
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Full Name
        panel.add(new JLabel("Full Name:"));
        fullNameField = new JTextField();
        panel.add(fullNameField);

        // DOB (yyyy-mm-dd)
        panel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        dobField = new JTextField();
        panel.add(dobField);

        // Gender
        panel.add(new JLabel("Gender:"));
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

        // Save button
        JButton saveButton = new JButton("Save");
        panel.add(saveButton);

        add(panel);

        // Button action
        saveButton.addActionListener(e -> savePatient());
    }

    private void savePatient() {
        String fullName = fullNameField.getText().trim();
        String dob = dobField.getText().trim();
        String gender = genderBox.getSelectedItem().toString();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressField.getText().trim();

        if (fullName.isEmpty() || dob.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Full Name and Date of Birth are required.");
            return;
        }

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO patients (full_name, dob, gender, phone, email, address) VALUES (?, ?, ?, ?, ?, ?)")) {

            stmt.setString(1, fullName);
            stmt.setString(2, dob); // Ensure format YYYY-MM-DD
            stmt.setString(3, gender);
            stmt.setString(4, phone);
            stmt.setString(5, email);
            stmt.setString(6, address);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Patient added successfully!");
                dispose(); // Close form after success
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }
}
