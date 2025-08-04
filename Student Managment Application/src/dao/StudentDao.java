package dao;

import model.Student;
import java.util.ArrayList;
import java.util.List;

public class StudentDao implements StudentDaoInterface {

    private List<Student> students = new ArrayList<>();
    private int rollCounter = 1; // To auto-increment roll numbers

    @Override
    public boolean insertStudent(Student s) {
        s.setRollNum(rollCounter++);
        students.add(s);
        return true;
    }

    @Override
    public boolean delete(int roll) {
        return students.removeIf(student -> student.getRollNum() == roll);
    }

    @Override
    public boolean update(int roll, String update, int ch, Student s) {
        for (Student student : students) {
            if (student.getRollNum() == roll) {
                switch (ch) {
                    case 1:
                        student.setName(update);
                        break;
                    case 2:
                        student.setClgName(update);
                        break;
                    case 3:
                        student.setCity(update);
                        break;
                    case 4:
                        student.setPercentage(Double.parseDouble(update));
                        break;
                    default:
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void showAllStudent() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        System.out.println("List of All Students:");
        for (Student s : students) {
            System.out.println(s);
        }
    }

    @Override
    public boolean showStudentById(int roll) {
        for (Student s : students) {
            if (s.getRollNum() == roll) {
                System.out.println(s);
                return true;
            }
        }
        System.out.println("Student not found with roll number: " + roll);
        return false;
    }
}
