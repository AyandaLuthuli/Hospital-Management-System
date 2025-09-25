package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    public LoginFrame() {
        setTitle("Hospital Login");
        setSize(350, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Build UI
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Role:"));
        roleBox = new JComboBox<>(new String[]{"Staff", "Patient"});
        panel.add(roleBox);

        JButton loginBtn = new JButton("Login");
        panel.add(loginBtn);

        add(panel);

        // Action on login button
        loginBtn.addActionListener(e -> checkLogin());
    }

    private void checkLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String selectedRole = roleBox.getSelectedItem().toString();

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT role FROM users WHERE username=? AND password_hash=? AND role=?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);  // ⚠️ use hashing in real projects
            stmt.setString(3, selectedRole.toUpperCase()); // DB should store roles in uppercase

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                JOptionPane.showMessageDialog(this,
                        "Welcome " + username + "! Role: " + role);

                // Role-based navigation
                if (role.equalsIgnoreCase("Staff")) {
                    new StaffDashboard().setVisible(true);
                } else if (role.equalsIgnoreCase("Patient")) {
                    new PatientDashboard().setVisible(true);
                }

                this.dispose(); // close login
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid username, password, or role.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Database error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
