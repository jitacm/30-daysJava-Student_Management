package dao;

import DB.DBconnection;
import model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentDao implements StudentDaoInterface {

    private Connection con;

    public StudentDao() {
        this.con = DBconnection.createConnection();
    }

    @Override
    public boolean insertStudent(Student s) {
        if (con == null) return false;
        try {
            String q = "INSERT INTO student(sname, s_clg_name, s_city, percentage) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = this.con.prepareStatement(q, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, s.getName());
            pstmt.setString(2, s.getClgName());
            pstmt.setString(3, s.getCity());
            pstmt.setDouble(4, s.getPercentage());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    s.setRollNum(rs.getInt(1)); // Set the generated roll number back to the student object
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int roll) {
        if (con == null) return false;
        try {
            String q = "DELETE FROM student WHERE roll_num = ?";
            PreparedStatement pstmt = this.con.prepareStatement(q);
            pstmt.setInt(1, roll);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(int roll, String updateValue, int ch, Student s) {
        if (con == null) return false;
        try {
            String q;
            PreparedStatement pstmt;
            switch (ch) {
                case 1: // Update name
                    q = "UPDATE student SET sname = ? WHERE roll_num = ?";
                    pstmt = this.con.prepareStatement(q);
                    pstmt.setString(1, updateValue);
                    pstmt.setInt(2, roll);
                    break;
                case 2: // Update college name
                    q = "UPDATE student SET s_clg_name = ? WHERE roll_num = ?";
                    pstmt = this.con.prepareStatement(q);
                    pstmt.setString(1, updateValue);
                    pstmt.setInt(2, roll);
                    break;
                case 3: // Update city
                    q = "UPDATE student SET s_city = ? WHERE roll_num = ?";
                    pstmt = this.con.prepareStatement(q);
                    pstmt.setString(1, updateValue);
                    pstmt.setInt(2, roll);
                    break;
                case 4: // Update percentage
                    q = "UPDATE student SET percentage = ? WHERE roll_num = ?";
                    pstmt = this.con.prepareStatement(q);
                    pstmt.setDouble(1, Double.parseDouble(updateValue));
                    pstmt.setInt(2, roll);
                    break;
                default:
                    return false;
            }
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Updated signature for update
    public boolean update(int roll, String name, String clgName, String city, double percentage) {
        if (con == null) return false;
        try {
            String q = "UPDATE student SET sname = ?, s_clg_name = ?, s_city = ?, percentage = ? WHERE roll_num = ?";
            PreparedStatement pstmt = this.con.prepareStatement(q);
            pstmt.setString(1, name);
            pstmt.setString(2, clgName);
            pstmt.setString(3, city);
            pstmt.setDouble(4, percentage);
            pstmt.setInt(5, roll);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void showAllStudent() {
        // This method is for console output, so we can implement a list return for GUI
    }
    
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        if (con == null) return students;
        try {
            String q = "SELECT * FROM student";
            Statement stmt = this.con.createStatement();
            ResultSet rs = stmt.executeQuery(q);

            while (rs.next()) {
                int rollNum = rs.getInt("roll_num");
                String name = rs.getString("sname");
                String clgName = rs.getString("s_clg_name");
                String city = rs.getString("s_city");
                double percentage = rs.getDouble("percentage");
                
                Student student = new Student(rollNum, name, clgName, city, percentage);
                students.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    public Map<String, Object> getStatisticalReport() {
    Map<String, Object> report = new HashMap<>();
    List<Student> students = getAllStudents();

    // Class ranking
    List<Student> rankedStudents = students.stream()
        .sorted(Comparator.comparingDouble(Student::getPercentage).reversed())
        .collect(Collectors.toList());

    // Pass/fail rates
    long passCount = students.stream().filter(s -> s.getPercentage() >= 40).count();
    long failCount = students.size() - passCount;

    report.put("classRanking", rankedStudents);
    report.put("passRate", (double) passCount / students.size() * 100);
    report.put("failRate", (double) failCount / students.size() * 100);
    report.put("totalStudents", students.size());

    return report;
}
    
    @Override
    public boolean showStudentById(int roll) {
        if (con == null) return false;
        try {
            String q = "SELECT * FROM student WHERE roll_num = ?";
            PreparedStatement pstmt = this.con.prepareStatement(q);
            pstmt.setInt(1, roll);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Returns true if student exists
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
