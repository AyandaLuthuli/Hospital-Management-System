import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import view.LoginFrame;

import java.sql.*;

public class Main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Failed to apply system look and feel" );
        }

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}