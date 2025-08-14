package gui;

import dao.StudentDao;
import model.Student;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardPanel extends JPanel {
    private JLabel totalStudentsLabel;
    private JLabel avgPercentageLabel;
    private JLabel passRateLabel;
    private JLabel failRateLabel;
    private StudentDao dao;

    public DashboardPanel(StudentDao dao) {
        this.dao = dao;
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(240, 245, 255));
        setBorder(new TitledBorder("Analytics Dashboard"));

        // Panel for holding the statistics
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Total Students Stat
        statsPanel.add(createStatPanel("Total Students", "0"));
        totalStudentsLabel = (JLabel) ((JPanel) statsPanel.getComponent(0)).getComponent(1);

        // Average Percentage Stat
        statsPanel.add(createStatPanel("Average Percentage", "0.00%"));
        avgPercentageLabel = (JLabel) ((JPanel) statsPanel.getComponent(1)).getComponent(1);

        // Pass Rate Stat
        statsPanel.add(createStatPanel("Pass Rate", "0.00%"));
        passRateLabel = (JLabel) ((JPanel) statsPanel.getComponent(2)).getComponent(1);

        // Fail Rate Stat
        statsPanel.add(createStatPanel("Fail Rate", "0.00%"));
        failRateLabel = (JLabel) ((JPanel) statsPanel.getComponent(3)).getComponent(1);

        add(statsPanel, BorderLayout.NORTH);

        // Create and add charts
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        chartsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        chartsPanel.setBackground(new Color(240, 245, 255));

        chartsPanel.add(createPercentageDistributionChart());
        chartsPanel.add(createCityDistributionChart());

        add(chartsPanel, BorderLayout.CENTER);
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

    private ChartPanel createPercentageDistributionChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Student> students = dao.getAllStudents();

        // Group students by percentage ranges
        Map<String, Integer> distribution = new HashMap<>();
        distribution.put("0-39", 0);
        distribution.put("40-59", 0);
        distribution.put("60-79", 0);
        distribution.put("80-100", 0);

        for (Student s : students) {
            if (s.getPercentage() < 40) distribution.put("0-39", distribution.get("0-39") + 1);
            else if (s.getPercentage() < 60) distribution.put("40-59", distribution.get("40-59") + 1);
            else if (s.getPercentage() < 80) distribution.put("60-79", distribution.get("60-79") + 1);
            else distribution.put("80-100", distribution.get("80-100") + 1);
        }

        for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
            dataset.addValue(entry.getValue(), "Students", entry.getKey());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Percentage Distribution",
                "Percentage Range",
                "Number of Students",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        return new ChartPanel(barChart);
    }

    private ChartPanel createCityDistributionChart() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        List<Student> students = dao.getAllStudents();

        Map<String, Integer> cityCount = new HashMap<>();
        for (Student s : students) {
            cityCount.put(s.getCity(), cityCount.getOrDefault(s.getCity(), 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : cityCount.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Student Distribution by City",
                dataset,
                true,
                true,
                false
        );

        return new ChartPanel(pieChart);
    }

    public void updateDashboard() {
        List<Student> students = dao.getAllStudents();
        int totalStudents = students.size();
        double totalPercentage = 0;
        int passCount = 0;
        int failCount = 0;

        for (Student s : students) {
            totalPercentage += s.getPercentage();
            if (s.getPercentage() >= 40) passCount++;
            else failCount++;
        }

        double avgPercentage = (totalStudents > 0) ? totalPercentage / totalStudents : 0.0;
        double passRate = (totalStudents > 0) ? (passCount * 100.0) / totalStudents : 0.0;
        double failRate = (totalStudents > 0) ? (failCount * 100.0) / totalStudents : 0.0;

        totalStudentsLabel.setText(String.valueOf(totalStudents));
        avgPercentageLabel.setText(String.format("%.2f%%", avgPercentage));
        passRateLabel.setText(String.format("%.2f%%", passRate));
        failRateLabel.setText(String.format("%.2f%%", failRate));
    }
}
