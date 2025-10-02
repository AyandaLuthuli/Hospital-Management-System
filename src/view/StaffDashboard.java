package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;

public class StaffDashboard extends JFrame {
    public StaffDashboard() {
        setTitle("Staff Dashboard");
        setSize(800, 600); // Increased size to accommodate side panel
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
        leftPanel.setBackground(new Color(240, 240, 240)); // Light gray background

        JButton addPatientBtn = new JButton("Add New Patient");
        styleButton(addPatientBtn);
        addPatientBtn.addActionListener(e -> new AddPatientForm().setVisible(true));
        leftPanel.add(addPatientBtn);

        JButton scheduleBtn = new JButton("Schedule Appointment");
        styleButton(scheduleBtn);
        scheduleBtn.addActionListener(e -> new ScheduleAppointmentForm().setVisible(true));
        leftPanel.add(scheduleBtn);

        JButton prescriptionBtn = new JButton("Manage Prescriptions");
        styleButton(prescriptionBtn);
        prescriptionBtn.addActionListener(e -> new ManagePrescriptionsForm().setVisible(true));
        leftPanel.add(prescriptionBtn);

        JButton searchBtn = new JButton("Search Records");
        styleButton(searchBtn);
        searchBtn.addActionListener(e -> new SearchRecordsForm().setVisible(true));
        leftPanel.add(searchBtn);

        JButton reportBtn = new JButton("Generate Reports");
        styleButton(reportBtn);
        reportBtn.addActionListener(e -> new GenerateReportsForm().setVisible(true));
        leftPanel.add(reportBtn);

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
        button.setForeground(Color.BLACK); // Changed to black
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(200, 50));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void styleLogoutButton(JButton button) {
        button.setBackground(new Color(220, 53, 69)); // Red
        button.setForeground(Color.BLACK); // Changed to black
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

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                // Scale image to fit panel - display original without any overlay
                int width = getWidth();
                int height = getHeight();
                g.drawImage(backgroundImage, 0, 0, width, height, this);

                // Add welcome text
                g.setColor(Color.BLACK); // Changed to black font
                g.setFont(new Font("SansSerif", Font.BOLD, 24));
                String welcomeText = "Welcome to Staff Dashboard";
                FontMetrics metrics = g.getFontMetrics();
                int x = (width - metrics.stringWidth(welcomeText)) / 2;
                int y = height / 2;
                g.drawString(welcomeText, x, y);
            }
        }

        private BufferedImage createFallbackBackground() {
            BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();

            // Create a medical-themed gradient background
            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(230, 240, 255), // Light blue
                    800, 600, new Color(200, 220, 255)  // Slightly darker blue
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, 800, 600);
            g2d.dispose();

            return image;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StaffDashboard().setVisible(true));
    }
}