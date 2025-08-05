package main;

import gui.LoginDialog;
import gui.StudentManagementGUI;

import javax.swing.*;

public class Client {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginDialog login = new LoginDialog(null);
            login.setVisible(true);
            if (login.isSucceeded()) {
                new StudentManagementGUI().setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}
