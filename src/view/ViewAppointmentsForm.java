package view;

import javax.swing.*;
import java.awt.*;

public class ViewAppointmentsForm extends JFrame {
    public ViewAppointmentsForm() {
        setTitle("Your Appointments");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea appointmentsArea = new JTextArea("Appointments will be listed here...");
        appointmentsArea.setEditable(false);
        panel.add(new JScrollPane(appointmentsArea), BorderLayout.CENTER);

        add(panel);
    }
}