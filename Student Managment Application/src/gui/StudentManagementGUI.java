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
    private StudentDaoInterface dao;
    private DefaultTableModel tableModel;
    private JTable studentTable;
    private JLabel statusLabel;

    public StudentManagementGUI() {
        dao = new StudentDao();

        setTitle("Student Management Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 540);
        setMinimumSize(new Dimension(700, 400)); // Optional, minimum size
        setLocationRelativeTo(null);
        setResizable(true);  // Changed to true for resizable window

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(new Color(240, 245, 255));

        // Header label
        JLabel heading = new JLabel("Student Management Application", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(13, 58, 113));
        JPanel headingPanel = new JPanel(new BorderLayout());
        headingPanel.setBackground(mainPanel.getBackground());
        headingPanel.add(heading, BorderLayout.CENTER);
        mainPanel.add(headingPanel, BorderLayout.NORTH);

        // Sidebar with buttons
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(mainPanel.getBackground());
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        RoundedButton addButton = new RoundedButton("Add Student", new Color(33, 150, 243));
        RoundedButton showButton = new RoundedButton("Show All Students", new Color(30, 136, 229));
        RoundedButton updateButton = new RoundedButton("Update Student", new Color(255, 193, 7));
        RoundedButton deleteButton = new RoundedButton("Delete Student", new Color(220, 53, 69));

        sidePanel.add(addButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 18)));
        sidePanel.add(updateButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 18)));
        sidePanel.add(deleteButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 18)));
        sidePanel.add(showButton);
        sidePanel.add(Box.createVerticalGlue());

        mainPanel.add(sidePanel, BorderLayout.WEST);

        // Search Panel
        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(20);
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Table to display students
        tableModel = new DefaultTableModel(new Object[] { "Roll No", "Name", "College", "City", "Percentage" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        // Sorting disabled by not setting row sorter
        studentTable.setRowHeight(32);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        studentTable.getTableHeader().setBackground(new Color(220, 220, 220));
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Disable column reordering and remove sorting arrows from UI
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

        // Zebra striping & center align percentage and roll columns
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
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Status bar
        statusLabel = new JLabel(" Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        statusLabel.setForeground(new Color(13, 58, 113));
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.add(statusLabel, BorderLayout.CENTER);
        statusBar.setBackground(mainPanel.getBackground());
        statusBar.setBorder(new EmptyBorder(6, 12, 6, 12));
        mainPanel.add(statusBar, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(this::showAddStudentDialog);
        showButton.addActionListener(e -> {
            refreshStudentTable();
            fadeStatus("Student list refreshed.");
        });
        updateButton.addActionListener(e -> showUpdateStudentDialog());
        deleteButton.addActionListener(e -> deleteSelectedStudent());

        // Search filter implementation: update table as user types
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void filter() {
                String query = searchField.getText().toLowerCase();

                List<Student> allStudents = getAllStudentsForTable();
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

    // Rounded button class with hover animation
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

    // Add Student Dialog with Enter key navigation
    private void showAddStudentDialog(ActionEvent e) {
        JTextField nameField = new JTextField();
        JTextField clgField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField percField = new JTextField();

        // Enter key focus navigation
        nameField.addActionListener(ae -> clgField.requestFocusInWindow());
        clgField.addActionListener(ae -> cityField.requestFocusInWindow());
        cityField.addActionListener(ae -> percField.requestFocusInWindow());
        // Optional: Perc field Enter key could trigger OK option but omitted here for simplicity

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
                dao.insertStudent(s);
                fadeStatus("Student \"" + name + "\" added successfully!");
                refreshStudentTable();
            } catch (Exception ex) {
                fadeStatus("Invalid input. Please check your entries.");
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(),
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Update Student Dialog with Enter key navigation
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

        // Enter key focus navigation
        nameField.addActionListener(ae -> clgField.requestFocusInWindow());
        clgField.addActionListener(ae -> cityField.requestFocusInWindow());
        cityField.addActionListener(ae -> percField.requestFocusInWindow());
        // Perc field Enter key could trigger OK option (optional)

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

                dao.update(rollNum, name, 1, null);
                dao.update(rollNum, clg, 2, null);
                dao.update(rollNum, city, 3, null);
                dao.update(rollNum, Double.toString(perc), 4, null);

                fadeStatus("Student \"" + name + "\" updated successfully!");
                refreshStudentTable();
            } catch (Exception ex) {
                fadeStatus("Invalid input. Please check your entries.");
                JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(),
                        "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Delete Selected Student
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

    private void refreshStudentTable() {
        tableModel.setRowCount(0);
        List<Student> students = getAllStudentsForTable();
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

    private List<Student> getAllStudentsForTable() {
        if (dao instanceof StudentDao) {
            try {
                java.lang.reflect.Field f = StudentDao.class.getDeclaredField("students");
                f.setAccessible(true);
                return new ArrayList<>((List<Student>) f.get(dao));
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    // Replace showStatus with fadeStatus for animated message appearance
    private void fadeStatus(String message) {
        statusLabel.setText(" " + message);
        // Start transparent
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
