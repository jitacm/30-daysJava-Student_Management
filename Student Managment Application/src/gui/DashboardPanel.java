package gui;

import dao.StudentDao;
import model.Student;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {

    private JLabel totalStudentsLabel;
    private JLabel avgPercentageLabel;
    private StudentDao dao;

    public DashboardPanel(StudentDao dao) {
        this.dao = dao;
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(240, 245, 255));
        setBorder(new TitledBorder("Analytics Dashboard"));

        // Panel for holding the statistics
        JPanel statsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Total Students Stat
        JPanel totalStudentsPanel = createStatPanel("Total Students", "0");
        totalStudentsLabel = (JLabel) totalStudentsPanel.getComponent(1);

        // Average Percentage Stat
        JPanel avgPercentagePanel = createStatPanel("Average Percentage", "0.00%");
        avgPercentageLabel = (JLabel) avgPercentagePanel.getComponent(1);

        statsPanel.add(totalStudentsPanel);
        statsPanel.add(avgPercentagePanel);

        add(statsPanel, BorderLayout.NORTH);
        updateDashboard();
    }

    private JPanel createStatPanel(String title, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panel.setPreferredSize(new Dimension(250, 80));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(13, 58, 113));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(new Color(33, 150, 243));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }

    public void updateDashboard() {
        List<Student> students = dao.getAllStudents();
        int totalStudents = students.size();
        double totalPercentage = 0;

        for (Student s : students) {
            totalPercentage += s.getPercentage();
        }

        double avgPercentage = (totalStudents > 0) ? totalPercentage / totalStudents : 0.0;

        totalStudentsLabel.setText(String.valueOf(totalStudents));
        avgPercentageLabel.setText(String.format("%.2f%%", avgPercentage));
    }
}