package view;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    public LoginFrame() {
        setTitle("Hospital Management System - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Role:"));
        roleBox = new JComboBox<>(new String[]{"Staff", "Patient"});
        panel.add(roleBox);

        JButton loginButton = new JButton("Login");
        panel.add(loginButton);

        loginButton.addActionListener(e -> {
            String role = roleBox.getSelectedItem().toString();
            if (role.equals("Staff")) {
                new StaffDashboard().setVisible(true);
            } else {
                new PatientDashboard().setVisible(true);
            }
            dispose();
        });

        add(panel);
    }
}