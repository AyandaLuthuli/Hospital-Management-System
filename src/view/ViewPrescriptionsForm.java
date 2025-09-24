package view;

import javax.swing.*;
import java.awt.*;

public class ViewPrescriptionsForm extends JFrame {
    public ViewPrescriptionsForm() {
        setTitle("Your Prescriptions");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea prescriptionsArea = new JTextArea("Prescriptions will be listed here...");
        prescriptionsArea.setEditable(false);
        panel.add(new JScrollPane(prescriptionsArea), BorderLayout.CENTER);

        add(panel);
    }
}