package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;

public class PatientDashboard extends JFrame {
    private long patientId;

    public PatientDashboard(long patientId) {
        this.patientId = patientId;

        setTitle("Patient Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Left Panel for buttons
        JPanel leftPanel = createLeftPanel();
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Right Panel for background picture
        BackgroundPanel rightPanel = new BackgroundPanel();
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        leftPanel.setPreferredSize(new Dimension(250, getHeight()));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        leftPanel.setBackground(new Color(240, 240, 240));

        JButton appointmentsBtn = new JButton("View Appointments");
        styleButton(appointmentsBtn);
        appointmentsBtn.addActionListener(e -> new ViewAppointmentsForm(patientId).setVisible(true));
        leftPanel.add(appointmentsBtn);

        JButton prescriptionsBtn = new JButton("View Prescriptions");
        styleButton(prescriptionsBtn);
        prescriptionsBtn.addActionListener(e -> new ViewPrescriptionsForm(patientId).setVisible(true));
        leftPanel.add(prescriptionsBtn);

        JButton doctorsBtn = new JButton("Search Doctors");
        styleButton(doctorsBtn);
        doctorsBtn.addActionListener(e -> new SearchDoctorsForm().setVisible(true));
        leftPanel.add(doctorsBtn);



        // Logout button
        JButton logoutBtn = new JButton("Logout");
        styleLogoutButton(logoutBtn);
        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });
        leftPanel.add(logoutBtn);

        return leftPanel;
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180)); // Steel blue
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(200, 50));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


    }

    private void styleLogoutButton(JButton button) {
        button.setBackground(new Color(220, 53, 69)); // Red
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(200, 50));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));



    }

    // Custom panel for background picture
    private class BackgroundPanel extends JPanel {
        private BufferedImage backgroundImage;

        public BackgroundPanel() {
            try {
                // Load a hospital/medical background image
                URL imageUrl = new URL("https://images.unsplash.com/photo-1551076805-e1869033e561?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2032&q=80");
                backgroundImage = ImageIO.read(imageUrl);
            } catch (Exception e) {
                // Fallback: create a gradient background
                System.err.println("Could not load background image: " + e.getMessage());
                backgroundImage = createFallbackBackground();
            }
        }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                // Scale image to fit panel
                int width = getWidth();
                int height = getHeight();
                g.drawImage(backgroundImage, 0, 0, width, height, this);

                // Add welcome text
                g.setColor(Color.BLACK); // Changed to black font
                g.setFont(new Font("SansSerif", Font.BOLD, 24));
                String welcomeText = "Welcome to Patient Dashboard";
                FontMetrics metrics = g.getFontMetrics();
                int x = (width - metrics.stringWidth(welcomeText)) / 2;
                int y = height / 2;
                g.drawString(welcomeText, x, y);

            }
        }

        private BufferedImage createFallbackBackground() {
            BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();

            // Create a patient-friendly gradient background
            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(230, 245, 255), // Very light blue
                    800, 600, new Color(200, 230, 255)  // Light blue
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, 800, 600);
            g2d.dispose();

            return image;
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PatientDashboard(12345L).setVisible(true));
    }
}