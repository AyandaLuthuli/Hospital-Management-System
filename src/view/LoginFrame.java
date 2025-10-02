package view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;
    private BackgroundPanel backgroundPanel;

    public LoginFrame() {
        setTitle("Hospital Login");
        setSize(400, 400); // Fixed size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Make the frame non-resizable
        setResizable(false);

        // Create background panel with blurred hospital image
        backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout(10, 10));

        // Title Label
        JLabel titleLabel = new JLabel("Hospital Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(false);
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);

        // Center Panel for Inputs
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false); // Make transparent
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        roleBox = new JComboBox<>(new String[]{"Staff", "Patient"});

        // Make labels white for better visibility
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        centerPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        centerPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        centerPanel.add(roleLabel, gbc);
        gbc.gridx = 1;
        centerPanel.add(roleBox, gbc);

        backgroundPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel for Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setOpaque(false); // Make transparent
        JButton loginBtn = new JButton("Login");
        JButton closeBtn = new JButton("Close");

        // Style Login Button (Blue with black text)
        loginBtn.setBackground(new Color(0, 123, 255)); // Blue background
        loginBtn.setForeground(Color.BLACK); // Black text
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginBtn.setOpaque(true);
        loginBtn.setBorderPainted(false);

        // Style Close Button (Red with black text)
        closeBtn.setBackground(new Color(220, 53, 69)); // Red background
        closeBtn.setForeground(Color.BLACK); // Black text
        closeBtn.setFocusPainted(false);
        closeBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        closeBtn.setOpaque(true);
        closeBtn.setBorderPainted(false);

        bottomPanel.add(loginBtn);
        bottomPanel.add(closeBtn);
        backgroundPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Set the background panel as content pane
        setContentPane(backgroundPanel);

        // Actions
        loginBtn.addActionListener(e -> checkLogin());
        closeBtn.addActionListener(e -> dispose());
    }

    // Custom panel for blurred background
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;
        private Image blurredImage;

        public BackgroundPanel() {
            try {
                // Load a hospital image from online source
                URL imageUrl = new URL("https://images.unsplash.com/photo-1586773860418-d37222d8fce3?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2073&q=80");
                backgroundImage = ImageIO.read(imageUrl);
                blurredImage = blurImage(backgroundImage);
            } catch (IOException e) {
                // Fallback: create a simple blue gradient background
                System.err.println("Could not load background image: " + e.getMessage());
                backgroundImage = createFallbackBackground();
                blurredImage = backgroundImage;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (blurredImage != null) {
                // Scale image to fit panel
                int width = getWidth();
                int height = getHeight();
                g.drawImage(blurredImage, 0, 0, width, height, this);

                // Add a semi-transparent overlay for better text readability
                g.setColor(new Color(0, 0, 0, 100)); // Black with 40% opacity
                g.fillRect(0, 0, width, height);
            }
        }

        private Image blurImage(Image image) {
            BufferedImage bufferedImage = new BufferedImage(
                    image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = bufferedImage.createGraphics();
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();

            // Very strong blur with 7x7 kernel
            int kernelSize = 7;
            float blurStrength = 1/(float)(kernelSize * kernelSize);
            float[] blurKernel = new float[kernelSize * kernelSize];
            java.util.Arrays.fill(blurKernel, blurStrength);

            ConvolveOp blurOp = new ConvolveOp(new Kernel(kernelSize, kernelSize, blurKernel));
            BufferedImage blurred = blurOp.filter(bufferedImage, null);

            // Apply multiple passes
            for (int i = 0; i < 3; i++) {
                blurred = blurOp.filter(blurred, null);
            }

            return blurred;
        }
        private Image createFallbackBackground() {
            BufferedImage image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();

            // Create a blue gradient background
            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(70, 130, 180), // Steel blue
                    400, 400, new Color(135, 206, 250) // Light sky blue
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, 400, 400);
            g2d.dispose();

            return image;
        }
    }

    private void checkLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String selectedRole = roleBox.getSelectedItem().toString();

        try (Connection conn = ConnectionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT role FROM users WHERE username=? AND password_hash=? AND role=?")) {

            stmt.setString(1, username);
            stmt.setString(2, password); // ⚠️ hash in production
            stmt.setString(3, selectedRole.toUpperCase());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                JOptionPane.showMessageDialog(this,
                        "Welcome " + username + "! Role: " + role);

                if (role.equalsIgnoreCase("STAFF")) {
                    new StaffDashboard().setVisible(true);
                } else if (role.equalsIgnoreCase("PATIENT")) {
                    long patientId = getPatientId(conn, username);
                    if (patientId != -1) {
                        new PatientDashboard(patientId).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Patient record not found for " + username);
                    }
                }

                this.dispose();
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

    private long getPatientId(Connection conn, String fullName) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT id FROM patients WHERE full_name = ?")) {
            stmt.setString(1, fullName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}