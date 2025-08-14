package gui;

import dao.StudentDao;
import dao.StudentDaoInterface;
import model.Student;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

public class StudentManagementGUI extends JFrame {
    private StudentDao dao;
    private DefaultTableModel tableModel;
    private JTable studentTable;
    private JLabel statusLabel;
    private JPanel cardsPanel; // This will hold the main view and the dashboard
    private CardLayout cardLayout; // Layout for switching panels
    private DashboardPanel dashboardPanel;

    public StudentManagementGUI() {
        dao = new StudentDao();
        
        setTitle("Student Management Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 540);
        setMinimumSize(new Dimension(700, 400));
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(new Color(240, 245, 255));

        JLabel heading = new JLabel("Student Management Application", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(13, 58, 113));
        JPanel headingPanel = new JPanel(new BorderLayout());
        headingPanel.setBackground(mainPanel.getBackground());
        headingPanel.add(heading, BorderLayout.CENTER);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(mainPanel.getBackground());
        topPanel.add(headingPanel, BorderLayout.NORTH);
        
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(mainPanel.getBackground());
        JTextField searchField = new JTextField(20);
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        topPanel.add(searchPanel, BorderLayout.SOUTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(mainPanel.getBackground());
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        RoundedButton dashboardButton = new RoundedButton("Dashboard", new Color(128, 128, 128));
        RoundedButton addButton = new RoundedButton("Add Student", new Color(33, 150, 243));
        RoundedButton showButton = new RoundedButton("Show All Students", new Color(30, 136, 229));
        RoundedButton updateButton = new RoundedButton("Update Student", new Color(255, 193, 7));
        RoundedButton deleteButton = new RoundedButton("Delete Student", new Color(220, 53, 69));
        RoundedButton exportCsvButton = new RoundedButton("Export to CSV", new Color(76, 175, 80));
        RoundedButton generatePdfButton = new RoundedButton("Generate PDF", new Color(255, 87, 34));

        sidePanel.add(dashboardButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 18)));
        sidePanel.add(addButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 18)));
        sidePanel.add(updateButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 18)));
        sidePanel.add(deleteButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 18)));
        sidePanel.add(showButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 18)));
        sidePanel.add(exportCsvButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 18)));
        sidePanel.add(generatePdfButton);
        sidePanel.add(Box.createVerticalGlue());

        mainPanel.add(sidePanel, BorderLayout.WEST);

        // Main content area with CardLayout
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);

        // Panel for the Student Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[] { "Roll No", "Name", "College", "City", "Percentage" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(32);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        studentTable.getTableHeader().setBackground(new Color(220, 220, 220));
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.getTableHeader().setReorderingAllowed(false);
        studentTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setFont(new Font("Segoe UI", Font.BOLD, 16));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBackground(new Color(220, 220, 220));
                label.setOpaque(true);
                label.setBorder(BorderFactory.createEmptyBorder());
                return label;
            }
        });

        studentTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(230, 240, 255));
                } else {
                    c.setBackground(new Color(179, 212, 255));
                }
                if (column == 0 || column == 4) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(33, 150, 243), 2),
                "Student List", 0, 0, new Font("Segoe UI", Font.BOLD, 16), new Color(33, 150, 243)));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Initialize and add the Dashboard Panel
        dashboardPanel = new DashboardPanel(dao);
        
        cardsPanel.add(tablePanel, "Table");
        cardsPanel.add(dashboardPanel, "Dashboard");
        mainPanel.add(cardsPanel, BorderLayout.CENTER);

        statusLabel = new JLabel(" Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        statusLabel.setForeground(new Color(13, 58, 113));
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.add(statusLabel, BorderLayout.CENTER);
        statusBar.setBackground(mainPanel.getBackground());
        statusBar.setBorder(new EmptyBorder(6, 12, 6, 12));
        mainPanel.add(statusBar, BorderLayout.SOUTH);

        // Button actions
        dashboardButton.addActionListener(e -> {
            dashboardPanel.updateDashboard(); // Update before showing
            cardLayout.show(cardsPanel, "Dashboard");
            fadeStatus("Switched to Dashboard.");
        });
        
        addButton.addActionListener(this::showAddStudentDialog);
        showButton.addActionListener(e -> {
            cardLayout.show(cardsPanel, "Table");
            refreshStudentTable();
            fadeStatus("Student list refreshed.");
        });
        updateButton.addActionListener(e -> showUpdateStudentDialog());
        deleteButton.addActionListener(e -> deleteSelectedStudent());
        exportCsvButton.addActionListener(e -> exportToCsv());
        generatePdfButton.addActionListener(e -> generatePdf());

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void filter() {
                String query = searchField.getText().toLowerCase();
                List<Student> allStudents = dao.getAllStudents();
                List<Student> filtered = new ArrayList<>();

                for (Student s : allStudents) {
                    if (s.getName().toLowerCase().contains(query) ||
                        Integer.toString(s.getRollNum()).contains(query) ||
                        s.getClgName().toLowerCase().contains(query) ||
                        s.getCity().toLowerCase().contains(query)) {
                        filtered.add(s);
                    }
                }
                tableModel.setRowCount(0);
                for (Student s : filtered) {
                    tableModel.addRow(new Object[] {
                        s.getRollNum(),
                        s.getName(),
                        s.getClgName(),
                        s.getCity(),
                        String.format("%.2f", s.getPercentage())
                    });
                }
            }
        });

        setContentPane(mainPanel);
    }
    
    // The rest of the methods remain the same

    static class RoundedButton extends JButton {
        private final Color bgColor;
        private final Color hoverColor;

        public RoundedButton(String text, Color bgColor) {
            super(text);
            this.bgColor = bgColor;
            this.hoverColor = bgColor.brighter();
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
            setBackground(bgColor);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseInputAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(bgColor);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            g2.dispose();
            super.paintComponent(g);
        }
    }
    
    private void showAddStudentDialog(ActionEvent e) {
        JTextField nameField = new JTextField();
        JTextField clgField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField percField = new JTextField();

        nameField.addActionListener(ae -> clgField.requestFocusInWindow());
        clgField.addActionListener(ae -> cityField.requestFocusInWindow());
        cityField.addActionListener(ae -> percField.requestFocusInWindow());
        
        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 7));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("College:"));
        panel.add(clgField);
        panel.add(new JLabel("City:"));
        panel.add(cityField);
        panel.add(new JLabel("Percentage:"));
        panel.add(percField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Student",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String clg = clgField.getText().trim();
                String city = cityField.getText().trim();
                double perc = Double.parseDouble(percField.getText().trim());
                if (name.isEmpty() || clg.isEmpty() || city.isEmpty())
                    throw new IllegalArgumentException("All fields are required.");
                Student s = new Student(name, clg, city, perc);
                boolean success = dao.insertStudent(s);
                if (success) {
                    fadeStatus("Student \"" + name + "\" added successfully!");
                    refreshStudentTable();
                } else {
                    fadeStatus("Failed to add student. Check database connection.");
                }
            } catch (Exception ex) {
                fadeStatus("Invalid input. Please check your entries.");
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(),
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showUpdateStudentDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            fadeStatus("Please select a student to update.");
            JOptionPane.showMessageDialog(this, "Select a student from the table first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int rollNum = (int) tableModel.getValueAt(selectedRow, 0);
        String currentName = (String) tableModel.getValueAt(selectedRow, 1);
        String currentClg = (String) tableModel.getValueAt(selectedRow, 2);
        String currentCity = (String) tableModel.getValueAt(selectedRow, 3);
        String currentPerc = tableModel.getValueAt(selectedRow, 4).toString();

        JTextField nameField = new JTextField(currentName);
        JTextField clgField = new JTextField(currentClg);
        JTextField cityField = new JTextField(currentCity);
        JTextField percField = new JTextField(currentPerc);

        nameField.addActionListener(ae -> clgField.requestFocusInWindow());
        clgField.addActionListener(ae -> cityField.requestFocusInWindow());
        cityField.addActionListener(ae -> percField.requestFocusInWindow());
        
        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 7));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("College:"));
        panel.add(clgField);
        panel.add(new JLabel("City:"));
        panel.add(cityField);
        panel.add(new JLabel("Percentage:"));
        panel.add(percField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Student",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String clg = clgField.getText().trim();
                String city = cityField.getText().trim();
                double perc = Double.parseDouble(percField.getText().trim());
                if (name.isEmpty() || clg.isEmpty() || city.isEmpty())
                    throw new IllegalArgumentException("All fields are required.");
                
                boolean success = dao.update(rollNum, name, clg, city, perc);

                if (success) {
                    fadeStatus("Student \"" + name + "\" updated successfully!");
                    refreshStudentTable();
                } else {
                    fadeStatus("Failed to update student. Check database connection.");
                }
            } catch (Exception ex) {
                fadeStatus("Invalid input. Please check your entries.");
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(),
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            fadeStatus("Please select a student to delete.");
            JOptionPane.showMessageDialog(this, "Select a student from the table first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int rollNum = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete \"" + name + "\"?",
                "Delete Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = dao.delete(rollNum);
            if (deleted) {
                fadeStatus("Student \"" + name + "\" deleted.");
                refreshStudentTable();
            } else {
                fadeStatus("Delete failed. Student not found.");
            }
        }
    }

    private void exportToCsv() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Student Data to CSV");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files (*.csv)", "csv"));
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".csv")) {
                fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".csv");
            }
            try {
                ExportUtil.exportToCsv(dao.getAllStudents(), fileToSave);
                fadeStatus("Student data exported to CSV successfully.");
            } catch (Exception ex) {
                fadeStatus("Error exporting data to CSV.");
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void generatePdf() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Generate Student Report PDF");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files (*.pdf)", "pdf"));
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".pdf")) {
                fileToSave = new java.io.File(fileToSave.getAbsolutePath() + ".pdf");
            }
            try {
                ExportUtil.generatePdfReport(dao.getAllStudents(), fileToSave);
                fadeStatus("PDF report generated successfully.");
            } catch (Exception ex) {
                fadeStatus("Error generating PDF report.");
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "PDF Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshStudentTable() {
        tableModel.setRowCount(0);
        List<Student> students = dao.getAllStudents();
        for (Student s : students) {
            tableModel.addRow(new Object[] {
                s.getRollNum(),
                s.getName(),
                s.getClgName(),
                s.getCity(),
                String.format("%.2f", s.getPercentage())
            });
        }
    }

    private void fadeStatus(String message) {
        statusLabel.setText(" " + message);
        statusLabel.setForeground(new Color(13, 58, 113, 0));
        Timer timer = new Timer(30, null);
        timer.addActionListener(new ActionListener() {
            int alpha = 0;
            public void actionPerformed(ActionEvent e) {
                alpha += 25;
                if (alpha >= 255) {
                    alpha = 255;
                    timer.stop();
                }
                statusLabel.setForeground(new Color(13, 58, 113, alpha));
            }
        });
        timer.start();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentManagementGUI frame = new StudentManagementGUI();
            frame.setVisible(true);
        });
    }
}